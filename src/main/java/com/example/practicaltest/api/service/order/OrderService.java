package com.example.practicaltest.api.service.order;

import com.example.practicaltest.api.controller.order.request.OrderCreateRequest;
import com.example.practicaltest.api.service.order.response.OrderResponse;
import com.example.practicaltest.domain.product.Product;
import com.example.practicaltest.domain.product.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;

    public OrderResponse createOrder(OrderCreateRequest request) {
        List<String> productNumbers = request.getProductNumbers();
        // product
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
        // order

        return null;
    }
}
