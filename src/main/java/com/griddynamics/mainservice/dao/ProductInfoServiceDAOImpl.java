package com.griddynamics.mainservice.dao;

import com.griddynamics.mainservice.Util.Logger;
import com.griddynamics.mainservice.domain.ProductInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Component
@Slf4j
public class ProductInfoServiceDAOImpl implements ProductInfoServiceDAO {

    private static final Integer TIMEOUT = 10;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8082/productInfoService")
            .build();

    @Override
    public Flux<ProductInfo> getByProductCode(String productCode, String requestId) {
        var uri = UriComponentsBuilder.fromUriString("/product/names")
                .queryParam("productCode", productCode)
                .toUriString();
        return webClient.get().uri(uri)
                .retrieve()
                .bodyToFlux(ProductInfo.class)
                .timeout(Duration.ofSeconds(TIMEOUT))
                .doOnEach(Logger
                        .logOnNext(v ->
                                log.info("RequestID[" + requestId + "]: Found productInfos: {}", v)))
                .doOnEach(Logger
                        .logOnError(e ->
                                log.error("RequestID[" + requestId + "]: Product info search error - " + e.getMessage())))
                .onErrorReturn(new ProductInfo());
    }


}
