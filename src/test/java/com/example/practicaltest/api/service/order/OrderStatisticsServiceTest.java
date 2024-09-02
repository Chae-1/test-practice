package com.example.practicaltest.api.service.order;

import static com.example.practicaltest.domain.product.ProductType.BAKERY;
import static com.example.practicaltest.domain.product.ProductType.BOTTLE;
import static com.example.practicaltest.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.practicaltest.client.MailSendClient;
import com.example.practicaltest.domain.history.mail.MailSendHistory;
import com.example.practicaltest.domain.history.mail.MailSendHistoryRepository;
import com.example.practicaltest.domain.order.Order;
import com.example.practicaltest.domain.order.OrderRepository;
import com.example.practicaltest.domain.order.OrderStatus;
import com.example.practicaltest.domain.orderproduct.OrderProductRepository;
import com.example.practicaltest.domain.product.Product;
import com.example.practicaltest.domain.product.ProductRepository;
import com.example.practicaltest.domain.product.ProductType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class OrderStatisticsServiceTest {

    @Autowired
    OrderStatisticsService orderStatisticsService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderProductRepository orderProductRepository;

    @Autowired
    MailSendHistoryRepository mailSendHistoryRepository;

    @MockBean
    MailSendClient mailSendClient;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        mailSendHistoryRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
    void sendOrderStatisticsMail() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 3, 5, 0, 0);

        Product product1 = createProduct(HANDMADE, "001", 1000);
        Product product2 = createProduct(HANDMADE, "002", 3000);
        Product product3 = createProduct(HANDMADE, "003", 5000);
        List<Product> products = List.of(product1, product2, product3);
        productRepository.saveAll(products);

        Order order1 = createPaymentCompleteOrder(LocalDateTime.of(2023, 3, 4, 23, 59, 59), products);
        Order order2 = createPaymentCompleteOrder(now, products); // 3월 5일에 해당하는 sum을 구한다.
        Order order3 = createPaymentCompleteOrder(LocalDateTime.of(2023, 3, 5, 23, 59, 59), products); //
        Order order4 = createPaymentCompleteOrder(LocalDateTime.of(2023, 3, 6, 0, 0), products);

        when(mailSendClient.sendEmail(any(String.class), any(String.class), any(String.class), any(String.class)))
                .thenReturn(true);
        // when
        boolean result = orderStatisticsService.sendOrderStatisticsMail(LocalDate.of(2023, 3, 5), "test@test.com");

        // then
        assertThat(result).isTrue();

        List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
        assertThat(histories).hasSize(1)
                .extracting("content")
                .contains("총 매출 합계는 18000원 입니다.");

    }

    private Order createPaymentCompleteOrder(LocalDateTime now, List<Product> products) {
        Order order = Order.builder()
                .products(products)
                .orderStatus(OrderStatus.COMPLETED)
                .registeredDateTime(now)
                .build();
        orderRepository.save(order);
        return order;
    }

    private Product createProduct(ProductType type, String productNumber, int price) {
        return Product.builder()
                .price(price)
                .productNumber(productNumber)
                .type(type)
                .name("메뉴 이름")
                .build();
    }
}