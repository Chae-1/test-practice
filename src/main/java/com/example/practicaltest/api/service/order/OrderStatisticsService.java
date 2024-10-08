package com.example.practicaltest.api.service.order;

import com.example.practicaltest.api.service.mail.MailService;
import com.example.practicaltest.domain.order.Order;
import com.example.practicaltest.domain.order.OrderRepository;
import com.example.practicaltest.domain.order.OrderStatus;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderStatisticsService {

    private final OrderRepository orderRepository;
    private final MailService mailService;

    public boolean sendOrderStatisticsMail(LocalDate orderDate, String email) {
        // 해당 일자에 결제완료된 주문들을 가져와서
        List<Order> orders = orderRepository.findOrdersBy(
                // 하루치 주문 정보를 전부 만들고 싶다.
                orderDate.atStartOfDay(),
                orderDate.plusDays(1).atStartOfDay(),
                OrderStatus.COMPLETED
        );
        // 총 매출 합계를 계산하고
        int totalAmount = orders.stream()
                .mapToInt(Order::getTotalPrice)
                .sum();
        // 메일 전송
        boolean result = mailService.sendMail(
                "no-reply@cafekiosk.com",
                email,
                String.format("[매출통계] %s", orderDate),
                String.format("총 매출 합계는 %s원 입니다.", totalAmount));

        if (!result) {
            throw new IllegalArgumentException("매출 통계 전송에 실패했습니다.");
        }

        return true;
    }
}
