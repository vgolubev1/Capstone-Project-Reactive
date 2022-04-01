package com.griddynamics.mainservice.dao;

import com.griddynamics.mainservice.Util.Logger;
import com.griddynamics.mainservice.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class OrderSearchServiceDAOImpl implements OrderSearchServiceDAO {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8081/orderSearchService")
            .build();

    @Override
    public Flux<Order> getByPhone(Mono<String> phone, String requestId) {
        var uri = UriComponentsBuilder.fromUriString("/order/phone")
                .queryParam("phoneNumber", phone)
                .toUriString();
        return webClient.get().uri(uri)
                .retrieve()
                .bodyToFlux(Order.class)
                .doOnEach(Logger.logOnNext(v -> log.info("RequestID[" + requestId + "]: Found orders: {}", v)))
                .doOnEach(Logger.logOnError(e -> log.error("RequestID[" + requestId + "]: Order search error - " + e.getMessage())));
    }
}
