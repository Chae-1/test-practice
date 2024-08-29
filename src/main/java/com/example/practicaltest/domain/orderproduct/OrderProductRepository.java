package com.example.practicaltest.domain.orderproduct;

import com.example.unit.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<Order, Long> {
}
