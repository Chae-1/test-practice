package com.example.practicaltest.api.service.product.response;

import com.example.practicaltest.domain.product.Product;
import com.example.practicaltest.domain.product.ProductSellingStatus;
import com.example.practicaltest.domain.product.ProductType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductResponse {

    private Long id;

    private String productNumber;

    private ProductType type;

    private ProductSellingStatus sellingType;

    private String name;

    private int price;

    @Builder
    private ProductResponse(Long id, String productNumber, ProductType type, ProductSellingStatus sellingType, String name,
                            int price) {
        this.id = id;
        this.productNumber = productNumber;
        this.type = type;
        this.sellingType = sellingType;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse of(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .productNumber(product.getProductNumber())
                .price(product.getPrice())
                .sellingType(product.getSellingStatus())
                .name(product.getName())
                .type(product.getType())
                .build();
    }
}
