package com.example.practicaltest.api.controller.product;

import com.example.practicaltest.api.ApiResponse;
import com.example.practicaltest.api.controller.product.dto.request.ProductCreateRequest;
import com.example.practicaltest.api.service.product.ProductService;
import com.example.practicaltest.api.service.product.response.ProductResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/api/v1/products/new")
    public ApiResponse<ProductResponse> create(@Validated @RequestBody ProductCreateRequest request) {
        ProductResponse productResponse = productService.createProduct(request.toServiceRequest());
        return ApiResponse.of(HttpStatus.OK, productResponse);
    }

    @GetMapping("/api/v1/products/selling")
    public ApiResponse<List<ProductResponse>> getSellingProducts() {
        return ApiResponse.ok(productService.getSellingProducts());
    }
}
