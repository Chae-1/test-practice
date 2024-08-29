package com.example.practicaltest.api.service.order.response;


import com.example.practicaltest.api.service.product.response.ProductResponse;
import com.example.practicaltest.domain.order.Order;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderResponse {

    private Long id;
    private int totalPrice;
    private LocalDateTime registeredDateTime;
    private List<ProductResponse> orderProducts;


    @Builder
    private OrderResponse(Long id, int totalPrice, LocalDateTime registeredDateTime, List<ProductResponse> orderProducts) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.registeredDateTime = registeredDateTime;
        this.orderProducts = orderProducts;
    }

    public static OrderResponse of(Order order, LocalDateTime registerDateTime) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderProducts(order.getOrderProducts()
                        .stream()
                        .map(orderProduct -> ProductResponse.of(orderProduct.getProduct()))
                        .toList())
                .totalPrice(order.getTotalPrice())
                .registeredDateTime(order.getRegisteredDateTime())
                .build();
    }
}
