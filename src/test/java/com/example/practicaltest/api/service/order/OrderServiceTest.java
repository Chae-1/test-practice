package com.example.practicaltest.api.service.order;

import static com.example.practicaltest.domain.product.ProductSellingStatus.HOLD;
import static com.example.practicaltest.domain.product.ProductSellingStatus.SELLING;
import static com.example.practicaltest.domain.product.ProductSellingStatus.STOP_SELLING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.*;

import com.example.practicaltest.api.controller.order.request.OrderCreateRequest;
import com.example.practicaltest.api.service.order.response.OrderResponse;
import com.example.practicaltest.domain.product.Product;
import com.example.practicaltest.domain.product.ProductRepository;
import com.example.practicaltest.domain.product.ProductType;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private ProductRepository productRepository;
    
    
    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrder() {
        // given
        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "002"))
                .build();

        Product product1 = createProduct(ProductType.HANDMADE, "001", 1000);
        Product product2 = createProduct(ProductType.HANDMADE, "001", 3000);
        Product product3 = createProduct(ProductType.HANDMADE, "001", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        OrderResponse response = orderService.createOrder(request);


        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response)
                .extracting("registeredDateTime", "totalPrice")
                .contains(LocalDateTime.now(), 4000);
        assertThat(response.getOrderProducts()).hasSize(2)
                .extracting("phoneNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 1000),
                        tuple("002", 3000)
                );
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