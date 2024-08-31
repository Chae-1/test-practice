package com.example.practicaltest.api.controller.product;

import static org.junit.jupiter.api.Assertions.*;

import com.example.practicaltest.api.service.product.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

// Controller layer만 테스트하기 위해 사용하는 애너테이션
@ActiveProfiles("test")
@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean // Mockito에서 만든 Bean을 주입하는 데 사용한다.
    private ProductService productService;

    @DisplayName("신규 상품을 생성한다.")
    @Test
    void test() {
        // given

        // when

        // then
    }

}