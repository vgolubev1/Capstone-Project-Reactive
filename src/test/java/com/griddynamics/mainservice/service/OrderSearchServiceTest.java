package com.griddynamics.mainservice.service;

import com.griddynamics.mainservice.dao.OrderSearchServiceDAO;
import com.griddynamics.mainservice.domain.Order;
import lombok.extern.slf4j.Slf4j;
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


@SpringBootTest
@ExtendWith(SpringExtension.class)
@Slf4j
class OrderSearchServiceTest {

    @Mock
    private OrderSearchServiceDAO orderSearchServiceDAO;

    @InjectMocks
    private OrderSearchServiceImpl orderSearchService;

    private static final String TEST_PHONE = "88005553535";
    private static final Mono<String> TEST_PHONE_MONO = Mono.just(TEST_PHONE);
    private static final String TEST_REQUEST_ID = "12345";


    private final Order testOrder1 = new Order("testOrderNumber1", null, TEST_PHONE, "11111", null, null);
    private final Order testOrder2 = new Order("testOrderNumber2", null, TEST_PHONE, "22222", null, null);
    private final Order testOrder3 = new Order("testOrderNumber3", null, TEST_PHONE, "33333", null, null);

    private final Flux<Order> expectedOrders = Flux.just(testOrder1, testOrder2, testOrder3);

    @Test
    void getByPhone() {
        Mockito.when(orderSearchServiceDAO.getByPhone(TEST_PHONE_MONO, TEST_REQUEST_ID)).thenReturn(expectedOrders);
        Flux<Order> actualOrder = orderSearchService.getByPhone(TEST_PHONE_MONO, TEST_REQUEST_ID);

        StepVerifier.create(actualOrder)
                .expectNext(testOrder1)
                .expectNext(testOrder2)
                .expectNext(testOrder3)
                .verifyComplete();
    }
}