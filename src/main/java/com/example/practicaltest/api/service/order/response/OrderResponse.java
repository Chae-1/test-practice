package com.example.practicaltest.api.service.order.response;


import com.example.practicaltest.api.service.product.response.ProductResponse;
import com.example.practicaltest.domain.order.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class OrderResponse {

    private Long id;
    private int totalPrice;
    private LocalDateTime registeredDateTime;
    private List<ProductResponse> orderProducts;

}
