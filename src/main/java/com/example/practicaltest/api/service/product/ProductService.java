package com.example.practicaltest.api.service.product;

import com.example.practicaltest.api.controller.product.dto.request.ProductCreateRequest;
import com.example.practicaltest.api.service.product.response.ProductResponse;
import com.example.practicaltest.domain.product.Product;
import com.example.practicaltest.domain.product.ProductRepository;
import com.example.practicaltest.domain.product.ProductSellingStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public void createProduct(ProductCreateRequest request) {

    }

    // 판매중, 판매 보류
    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

        return products.stream()
                .map(product -> ProductResponse.of(product))
                .toList();
    }
}
