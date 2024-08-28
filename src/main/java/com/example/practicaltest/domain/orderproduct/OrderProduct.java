package com.example.practicaltest.domain.orderproduct;

import com.example.practicaltest.domain.BaseEntity;
import com.example.practicaltest.domain.order.Order;
import com.example.practicaltest.domain.product.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrderProduct extends BaseEntity {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Builder
    private OrderProduct(Order order, Product product) {
        this.order = order;
        this.product = product;
    }
}
