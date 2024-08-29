package com.example.practicaltest.api.service.order;

import com.example.practicaltest.api.controller.order.request.OrderCreateRequest;
import com.example.practicaltest.api.service.order.response.OrderResponse;
import com.example.practicaltest.domain.order.Order;
import com.example.practicaltest.domain.order.OrderRepository;
import com.example.practicaltest.domain.product.Product;
import com.example.practicaltest.domain.product.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registerDateTime) {
        List<String> productNumbers = request.getProductNumbers();
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
        
        // key : Product
        List<Product> duplicateProducts = findProductsBy(products, productNumbers);


        Order order = Order.create(duplicateProducts, registerDateTime);
        Order saveOrder = orderRepository.save(order);

        return OrderResponse.of(saveOrder, registerDateTime);
    }

    private static List<Product> findProductsBy(List<Product> products, List<String> productNumbers) {
        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductNumber, p -> p));

        List<Product> duplicateProducts = productNumbers
                .stream()
                .map(productMap::get)
                .toList();
        return duplicateProducts;
    }
}
