package com.example.practicaltest.api.service.product;

import com.example.practicaltest.api.controller.product.dto.request.ProductCreateRequest;
import com.example.practicaltest.api.service.product.response.ProductResponse;
import com.example.practicaltest.domain.product.Product;
import com.example.practicaltest.domain.product.ProductRepository;
import com.example.practicaltest.domain.product.ProductSellingStatus;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * readOnly = true : 읽기 전용
 * JPA : CUD 스냅샷 저장. 변경감지 X
 *
 * CQRS - Command / Read
 *
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    // 동시성 이슈
    // UUID
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        // nextProductNumber
        String nextProductNumber = createNextProductNumber();


        Product product = request.toEntity(nextProductNumber);
        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
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
            return "001";
        }

        int latestProductNumberInt = Integer.parseInt(latestProductNumber);
        int nextProductNumberInt = latestProductNumberInt + 1;
        return String.format("%03d", nextProductNumberInt);
    }
}
