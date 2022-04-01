package com.griddynamics.mainservice.service;

import com.griddynamics.mainservice.domain.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderSearchService {
    Flux<Order> getByPhone(Mono<String> phone, String requestId);
}
