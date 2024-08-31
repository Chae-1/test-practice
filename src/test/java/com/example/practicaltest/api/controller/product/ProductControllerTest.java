package com.example.practicaltest.api.controller.product;

import static com.example.practicaltest.domain.product.ProductSellingStatus.SELLING;
import static com.example.practicaltest.domain.product.ProductType.HANDMADE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.practicaltest.api.controller.product.dto.request.ProductCreateRequest;
import com.example.practicaltest.api.service.product.ProductService;
import com.example.practicaltest.api.service.product.response.ProductResponse;
import com.example.practicaltest.domain.product.ProductSellingStatus;
import com.example.practicaltest.domain.product.ProductType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("OK"));;

    }

    @DisplayName("신규 상품을 등록할 때 상품 타입은 필수값이다.")
    @Test
    void createProductWithoutType() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("OK"));

    }

    @DisplayName("신규 상품을 등록할 때 상품 판매상태는 필수값이다.")
    @Test
    void createProductWithoutSellingStatus() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .name("아메리카노")
                .type(HANDMADE)
                .price(4000)
                .build();

        //when , // then
        mockMvc.perform(
                        post("/api/v1/products/new")
                                .content(mapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("상품 판매상태는 필수입니다."));

    }

    @DisplayName("신규 상품을 등록할 때 상품가격은 양수여야한다.")
    @Test
    void createProductWithZeroPrice() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .name("아메리카노")
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .price(0)
                .build();

        //when , // then
        mockMvc.perform(
                        post("/api/v1/products/new")
                                .content(mapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("상품 가격은 양수여야합니다."));

    }

    @Test
    @DisplayName("신규 상품을 등록할 때 이름은 필수이다.")
    void createProductWithoutName() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .sellingStatus(SELLING)
                .type(HANDMADE)
                .price(4000)
                .build();

        //when , // then
        mockMvc.perform(
                        post("/api/v1/products/new")
                                .content(mapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("상품 이름은 필수입니다."));

    }


    @DisplayName("판매 상품을 조회한다.")
    @Test
    void getSellingProducts() throws Exception {
        // given
        List<ProductResponse> result = List.of();
        when(productService.getSellingProducts()).thenReturn(result);
        // when
        // then
        mockMvc.perform(
                        get("/api/v1/products/selling")
                )
                .andDo(print())
                .andExpect(jsonPath("$.data").isArray());
    }

}