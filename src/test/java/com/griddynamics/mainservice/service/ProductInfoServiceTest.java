package com.griddynamics.mainservice.service;

import com.griddynamics.mainservice.dao.ProductInfoServiceDAO;
import com.griddynamics.mainservice.domain.ProductInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest()
@ExtendWith(SpringExtension.class)
class ProductInfoServiceTest {

    @Mock
    ProductInfoServiceDAO productInfoServiceDAO;

    @InjectMocks
    private ProductInfoServiceImpl productInfoService;

    private static final String TEST_PRODUCT_CODE = "213";
    private static final String TEST_REQUEST_ID = "12345";

    private final ProductInfo testProductInfo1 = new ProductInfo("testProductId1", TEST_PRODUCT_CODE, "testProductName1", 1.0f);
    private final ProductInfo testProductInfo2 = new ProductInfo("testProductId2", TEST_PRODUCT_CODE, "testProductName2", 2.0f);
    private final ProductInfo testProductInfo3 = new ProductInfo("testProductId3", TEST_PRODUCT_CODE, "testProductName3", 3.0f);

    private final Flux<ProductInfo> expectedProductInfo = Flux.just(testProductInfo1, testProductInfo2, testProductInfo3);
    private final Flux<ProductInfo> expectedProductInfoServiceOff = Flux.just(new ProductInfo());


    @Test
    void getByProductCode() {
        Mockito.when(productInfoServiceDAO.getByProductCode(TEST_PRODUCT_CODE, TEST_REQUEST_ID)).thenReturn(expectedProductInfo);

        Flux<ProductInfo> actualProductInfo = productInfoService.getByProductCode(TEST_PRODUCT_CODE, TEST_REQUEST_ID);

        StepVerifier.create(actualProductInfo)
                .expectNext(testProductInfo1)
                .expectNext(testProductInfo2)
                .expectNext(testProductInfo3)
                .verifyComplete();
    }

    @Test
    void getByProductCodeWhenServiceIsTurnOff() {
        Mockito.when(productInfoServiceDAO.getByProductCode(TEST_PRODUCT_CODE, TEST_REQUEST_ID)).thenReturn(expectedProductInfoServiceOff);

        Flux<ProductInfo> actualProductInfo = productInfoService.getByProductCode(TEST_PRODUCT_CODE, TEST_REQUEST_ID);

        StepVerifier.create(actualProductInfo)
                .expectNext(new ProductInfo())
                .verifyComplete();
    }


    @Test
    void getMostRelevant() {

        Mono<ProductInfo> actualMostRelevantProductInfo = productInfoService.getMostRelevant(expectedProductInfo, TEST_REQUEST_ID);

        StepVerifier.create(actualMostRelevantProductInfo)
                .expectNext(testProductInfo3)
                .verifyComplete();
    }

    @Test
    void getMostRelevantWhenServiceTurnOff() {

        Mono<ProductInfo> actualMostRelevantProductInfo = productInfoService.getMostRelevant(expectedProductInfoServiceOff, TEST_REQUEST_ID);

        StepVerifier.create(actualMostRelevantProductInfo)
                .expectNext(new ProductInfo())
                .verifyComplete();
    }
}