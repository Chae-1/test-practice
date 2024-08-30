package com.example.practicaltest.domain.product;

import static com.example.practicaltest.domain.product.ProductSellingStatus.HOLD;
import static com.example.practicaltest.domain.product.ProductSellingStatus.SELLING;
import static com.example.practicaltest.domain.product.ProductSellingStatus.STOP_SELLING;
import static com.example.practicaltest.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @DisplayName("원하는 판매 상태를 가진 상품들을 조회한다.")
    @Test
    void findAllBySellingStatusIn() {
        // given

        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
        Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);
        productRepository.saveAll(List.of(product1, product2, product3));
        // when

        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));
        // then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );
    }

    @DisplayName("상품 번호 리스트로 상품들을 조회한다.")
    @Test
    void findAllByProductNumberIn() {
        // given

        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
        Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);
        productRepository.saveAll(List.of(product1, product2, product3));
        // when

        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));
        // then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );
    }

    @DisplayName("가장 마지막에 저장한 상품 번호를 조회한다.")
    @Test
    void findLatestProductNumber() {
        // given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
        Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);

        String targetProductNumber = "003";

        productRepository.saveAll(List.of(product1, product2, product3));
        // when
        String productNumber = productRepository.findLatestProductNumber();
        // then
        assertThat(productNumber).isEqualTo(targetProductNumber);

    }

    @DisplayName("가장 마지막에 저장한 상품 번호를 조회한다.")
    @Test
    void findLatestProductNumberWhenProductIsEmpty() {
        // when
        String productNumber = productRepository.findLatestProductNumber();
        assertThat(productNumber).isNull();
    }


    private Product createProduct(String productNumber, ProductType productType, ProductSellingStatus sellingStatus,
                                  String name, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(productType)
                .sellingStatus(sellingStatus)
                .name(name)
                .price(price)
                .build();
    }


}