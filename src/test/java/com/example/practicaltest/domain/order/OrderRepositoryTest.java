package com.example.practicaltest.domain.order;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;


    @DisplayName("해당하는 일자, 상태에 맞는 모든 주문을 가지고 온다.")
    @Test
    void findOrdersBy() {
        // given
        Order order = new Order();


        // when
        // then
    }


}