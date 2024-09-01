package com.example.practicaltest.api.controller.order.request;

import java.util.List;

import com.example.practicaltest.api.service.order.request.OrderCreateServiceRequest;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderCreateRequest {

    @NotEmpty(message = "상품은 하나 이상 선택해야합니다.")
    private List<String> productNumbers;

    @Builder
    private OrderCreateRequest(List<String> productNumbers) {
        this.productNumbers = productNumbers;
    }

    public OrderCreateServiceRequest toServiceRequest() {
        return OrderCreateServiceRequest
                .builder()
                .productNumbers(productNumbers)
                .build();
    }
}
