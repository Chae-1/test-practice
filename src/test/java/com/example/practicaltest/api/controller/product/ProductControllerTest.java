package com.example.practicaltest.api.controller.product;

import static com.example.practicaltest.domain.product.ProductSellingStatus.SELLING;
import static com.example.practicaltest.domain.product.ProductType.HANDMADE;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.practicaltest.api.controller.product.dto.request.ProductCreateRequest;
import com.example.practicaltest.api.service.product.ProductService;
import com.example.practicaltest.domain.product.ProductSellingStatus;
import com.example.practicaltest.domain.product.ProductType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

// Controller layer만 테스트하기 위해 사용하는 애너테이션
@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Mockito에서 만든 Bean을 주입하는 데 사용한다.
    private ProductService productService;

    @Autowired
    ObjectMapper mapper;

    @DisplayName("신규 상품을 생성한다.")
    @Test
    void createProduct() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .name("아메리카노")
                .sellingStatus(SELLING)
                .price(4000)
                .build();

        //when , // then
        mockMvc.perform(
                post("/api/v1/products/new")
                    .content(mapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());

    }

}