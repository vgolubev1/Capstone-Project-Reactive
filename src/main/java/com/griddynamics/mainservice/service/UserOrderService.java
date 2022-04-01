package com.griddynamics.mainservice.service;

import com.griddynamics.mainservice.domain.Order;
import reactor.core.publisher.Flux;

public interface UserOrderService {
    Flux<Order> getUserOrderWithRelevantProduct(String requestID, String userId);
}
