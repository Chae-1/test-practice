package com.example.practicaltest.api.controller.product.dto.request;

import com.example.practicaltest.domain.product.Product;
import com.example.practicaltest.domain.product.ProductSellingStatus;
import com.example.practicaltest.domain.product.ProductType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

    private ProductType type;
    private ProductSellingStatus sellingStatus;
    private String name;
    private int price;

    @Builder
    public ProductCreateRequest(ProductType type, ProductSellingStatus sellingStatus, String name, int price) {

        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }

    public Product toEntity(String nextProductNumber) {
        return Product.builder()
                .productNumber(nextProductNumber)
                .type(type)
                .name(name)
                .price(price)
                .sellingStatus(sellingStatus)
                .build();
    }
}
