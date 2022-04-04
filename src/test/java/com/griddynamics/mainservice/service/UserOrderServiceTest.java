package com.griddynamics.mainservice.service;

import com.griddynamics.mainservice.Repository.UserInfoRepository;
import com.griddynamics.mainservice.domain.Order;
import com.griddynamics.mainservice.domain.ProductInfo;
import com.griddynamics.mainservice.domain.User;
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
class UserOrderServiceTest {

    private static final String TEST_PHONE = "88005553535";
    private static final String TEST_USER_ID = "testUserId";
    public static final String TEST_USER_NAME = "testUserName";

    public static final String TEST_REQUEST_ID = "111111";
    public static final String TEST_PRODUCT_CODE_1 = "11111";
    public static final String TEST_PRODUCT_CODE_2 = "22222";
    public static final String TEST_PRODUCT_CODE_3 = "33333";

    private final User testUser = new User(TEST_USER_ID, TEST_USER_NAME, TEST_PHONE);

    private final Order testOrder1 = new Order("testOrderNumber1", null, TEST_PHONE, TEST_PRODUCT_CODE_1, null, null);
    private final Order testOrder2 = new Order("testOrderNumber2", null, TEST_PHONE, TEST_PRODUCT_CODE_2, null, null);
    private final Order testOrder3 = new Order("testOrderNumber3", null, TEST_PHONE, TEST_PRODUCT_CODE_3, null, null);
    private final Flux<Order> testOrderFlux = Flux.just(testOrder1, testOrder2, testOrder3);

    private final ProductInfo testProductInfo1TestOrder1 = new ProductInfo("testProductId1", TEST_PRODUCT_CODE_1, "testProductName1", 1.0f);
    private final ProductInfo testProductInfo2TestOrder1 = new ProductInfo("testProductId2", TEST_PRODUCT_CODE_1, "testProductName2", 2.0f);
    private final ProductInfo testProductInfo3TestOrder1 = new ProductInfo("testProductId3", TEST_PRODUCT_CODE_1, "testProductName3", 3.0f);
    private final ProductInfo testProductInfo3TestOrder2 = new ProductInfo("testProductId3", TEST_PRODUCT_CODE_2, "testProductName3", 3.0f);
    private final ProductInfo testProductInfo3TestOrder3 = new ProductInfo("testProductId3", TEST_PRODUCT_CODE_3, "testProductName3", 3.0f);

    private final Flux<ProductInfo> testProductInfoOrder1Flux = Flux.just(testProductInfo1TestOrder1, testProductInfo2TestOrder1, testProductInfo3TestOrder1);
    private final Flux<ProductInfo> testProductInfoEmptyFlux = Flux.just(new ProductInfo());


    private final Order expectedOrder1 = new Order("testOrderNumber1", TEST_USER_NAME, TEST_PHONE, TEST_PRODUCT_CODE_1, testProductInfo3TestOrder1.getProductName(), testProductInfo3TestOrder1.getProductId());
    private final Order expectedOrder2 = new Order("testOrderNumber2", TEST_USER_NAME, TEST_PHONE, TEST_PRODUCT_CODE_2, testProductInfo3TestOrder2.getProductName(), testProductInfo3TestOrder2.getProductId());
    private final Order expectedOrder3 = new Order("testOrderNumber3", TEST_USER_NAME, TEST_PHONE, TEST_PRODUCT_CODE_3, testProductInfo3TestOrder3.getProductName(), testProductInfo3TestOrder3.getProductId());

    private final Order expectedOrder1ProductServiceIsTurnOff = new Order("testOrderNumber1", TEST_USER_NAME, TEST_PHONE, TEST_PRODUCT_CODE_1, null, null);
    private final Order expectedOrder2ProductServiceIsTurnOff = new Order("testOrderNumber2", TEST_USER_NAME, TEST_PHONE, TEST_PRODUCT_CODE_2, null, null);
    private final Order expectedOrder3ProductServiceIsTurnOff = new Order("testOrderNumber3", TEST_USER_NAME, TEST_PHONE, TEST_PRODUCT_CODE_3, null, null);

    @Mock
    private UserInfoRepository userInfoRepository;

    @Mock
    private OrderSearchService orderSearchService;

    @Mock
    private ProductInfoService productInfoService;

    @InjectMocks
    private UserOrderServiceImpl userOrderService;

    @Test
    void getUserOrderWithRelevantProduct() {

        Mockito.when(userInfoRepository.findById(TEST_USER_ID))
                .thenReturn(Mono.just(testUser));
        Mockito.when(orderSearchService.getByPhone(Mockito.any(), Mockito.any()))
                .thenReturn(testOrderFlux);
        Mockito.when(productInfoService.getByProductCode(Mockito.any(), Mockito.any()))
                .thenReturn(testProductInfoOrder1Flux);
        Mockito.when(productInfoService.getMostRelevant(testProductInfoOrder1Flux, TEST_REQUEST_ID))
                .thenReturn(Mono.just(testProductInfo3TestOrder1));

        Flux<Order> actualOrderWithRelevantProduct = userOrderService.getUserOrderWithRelevantProduct(TEST_REQUEST_ID, TEST_USER_ID);
        StepVerifier.create(actualOrderWithRelevantProduct)
                .expectNext(expectedOrder1)
                .expectNext(expectedOrder2)
                .expectNext(expectedOrder3)
                .verifyComplete();
    }

    @Test
    void getUserOrderWithRelevantProductProductInfoServiceIsTurnOff() {
        Mockito.when(userInfoRepository.findById(TEST_USER_ID))
                .thenReturn(Mono.just(testUser));
        Mockito.when(orderSearchService.getByPhone(Mockito.any(), Mockito.any()))
                .thenReturn(testOrderFlux);
        Mockito.when(productInfoService.getByProductCode(Mockito.any(), Mockito.any()))
                .thenReturn(testProductInfoEmptyFlux);
        Mockito.when(productInfoService.getMostRelevant(Mockito.any(), Mockito.any()))
                .thenReturn(Mono.just(new ProductInfo()));

        Flux<Order> actualOrderWithRelevantProduct = userOrderService.getUserOrderWithRelevantProduct(TEST_REQUEST_ID, TEST_USER_ID);
        StepVerifier.create(actualOrderWithRelevantProduct)
                .expectNext(expectedOrder1ProductServiceIsTurnOff)
                .expectNext(expectedOrder2ProductServiceIsTurnOff)
                .expectNext(expectedOrder3ProductServiceIsTurnOff)
                .verifyComplete();
    }
}