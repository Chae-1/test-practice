package com.example.practicaltest.api.service.product;

import com.example.practicaltest.api.controller.product.dto.request.ProductCreateRequest;
import com.example.practicaltest.api.service.product.response.ProductResponse;
import com.example.practicaltest.domain.product.Product;
import com.example.practicaltest.domain.product.ProductRepository;
import com.example.practicaltest.domain.product.ProductSellingStatus;
import java.util.List;

import com.example.practicaltest.domain.product.ProductType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import static com.example.practicaltest.domain.product.ProductSellingStatus.SELLING;
import static com.example.practicaltest.domain.product.ProductType.HANDMADE;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    // 동시성 이슈
    // UUID
    public ProductResponse createProduct(ProductCreateRequest request) {
        // nextProductNumber
        String nextProductNumber = createNextProductNumber();

        return ProductResponse.builder()
                .productNumber(nextProductNumber)
                .type(HANDMADE)
                .sellingType(SELLING)
                .name("카푸치노")
                .price(5000)
                .build();
    }

    // 판매중, 판매 보류
    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

        return products.stream()
                .map(product -> ProductResponse.of(product))
                .toList();
    }

    public String createNextProductNumber() {
        String latestProductNumber = productRepository.findLatestProductNumber();
        if (latestProductNumber == null) {
            latestProductNumber = "001";
        }

        int latestProductNumberInt = Integer.parseInt(latestProductNumber);
        int nextProductNumberInt = latestProductNumberInt + 1;
        return String.format("%03d", nextProductNumberInt);
    }
}
