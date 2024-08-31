package com.example.practicaltest.api.service.order;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

import com.example.practicaltest.api.controller.order.request.OrderCreateRequeset;
import com.example.practicaltest.api.service.order.request.OrderCreateServiceRequest;
import com.example.practicaltest.api.service.order.response.OrderResponse;
import com.example.practicaltest.domain.order.Order;
import com.example.practicaltest.domain.order.OrderRepository;
import com.example.practicaltest.domain.product.Product;
import com.example.practicaltest.domain.product.ProductRepository;

import com.example.practicaltest.domain.product.ProductType;
import com.example.practicaltest.domain.stock.Stock;
import com.example.practicaltest.domain.stock.StockRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;

    /**
     * 재고 감소 -> 동시성 고민
     * optimistic lock / pessimistic lock / ...
     * 먼저 요청한 순으로 처리할 수 있도록 처리 고려.
     */
    @Transactional
    public OrderResponse createOrder(OrderCreateServiceRequest request, LocalDateTime registerDateTime) {
        List<String> productNumbers = request.getProductNumbers();
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);

        deductStockQuantities(productNumbers, products);

        List<Product> duplicateProducts = findProductsBy(products, productNumbers);


        Order order = Order.create(duplicateProducts, registerDateTime);
        Order saveOrder = orderRepository.save(order);

        return OrderResponse.of(saveOrder, registerDateTime);
    }

    private void deductStockQuantities(List<String> productNumbers, List<Product> products) {
        // 재고 체크가 필요한 상품들 filter
        List<String> stockProductNumbers = extractStockProductNumbers(products);

        // 재고 엔터티 조회
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);
        Map<String, Stock> stockMap = createStockMapBy(stocks);

        // 상품별 counting
        Map<String, Long> productCountingMap = createCountingMapBy(productNumbers);

        // 재고 차감 시도
        for (String productNumber : stockProductNumbers) {
            Stock stock = stockMap.get(productNumber);
            int quantity = productCountingMap.get(productNumber).intValue();

            if (stock.isQuantityLessThen(quantity)) {
                throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
            }
            stock.deductQuantity(quantity);
        }
    }

    private Map<String, Long> createCountingMapBy(List<String> productNumbers) {
        return productNumbers.stream()
                .collect(groupingBy(num -> num, Collectors.counting()));
    }

    private Map<String, Stock> createStockMapBy(List<Stock> stocks) {
        return stocks.stream()
                .collect(toMap(Stock::getProductNumber, s -> s));
    }

    private List<String> extractStockProductNumbers(List<Product> products) {
        List<String> stockProductNumbers = products.stream()
                .filter(product -> ProductType.containsStockType(product.getType()))
                .map(Product::getProductNumber)
                .toList();
        return stockProductNumbers;
    }

    private List<Product> findProductsBy(List<Product> products, List<String> productNumbers) {
        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductNumber, p -> p));

        List<Product> duplicateProducts = productNumbers
                .stream()
                .map(productMap::get)
                .toList();

        return duplicateProducts;
    }
}
