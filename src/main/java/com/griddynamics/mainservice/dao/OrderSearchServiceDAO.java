package com.griddynamics.mainservice.dao;

import com.griddynamics.mainservice.domain.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface OrderSearchServiceDAO {
    Flux<Order> getByPhone(Mono<String> phone, String requestId);
}
