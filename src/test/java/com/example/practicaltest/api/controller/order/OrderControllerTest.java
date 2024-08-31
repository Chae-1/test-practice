package com.example.practicaltest.api.controller.order;

import com.example.practicaltest.api.controller.order.request.OrderCreateRequeset;
import com.example.practicaltest.api.service.order.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    @Autowired
    ObjectMapper mapper;

    @DisplayName("상품 번호들로 상품을 주문한다.")
    @Test
    void createOrder() throws Exception {
        // given
        OrderCreateRequeset request = OrderCreateRequeset.builder()
                .productNumbers(List.of("001"))
                .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/orders/new").contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"));
    }

    @DisplayName("상품을 1개 이상 선택해야 주문할 수 있다.")
    @Test
    void createOrderWithoutProduct() throws Exception {
        // given
        OrderCreateRequeset request = OrderCreateRequeset.builder()
                .productNumbers(List.of())
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/orders/new")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품은 하나 이상 선택해야합니다."));
    }

}