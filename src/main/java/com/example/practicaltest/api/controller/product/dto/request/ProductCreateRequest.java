package com.example.practicaltest.api.controller.product.dto.request;

import com.example.practicaltest.domain.product.ProductSellingStatus;
import com.example.practicaltest.domain.product.ProductType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class ProductCreateRequest {

    private String productNumber;
    private ProductType type;
    private ProductSellingStatus sellingStatus;
    private String name;
    private int price;

}
