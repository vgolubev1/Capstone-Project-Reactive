package com.griddynamics.mainservice.service;

import com.griddynamics.mainservice.domain.ProductInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductInfoService {
    Flux<ProductInfo> getByProductCode(String productCode, String requestId);

    Mono<ProductInfo> getMostRelevant(Flux<ProductInfo> productInfoFlux, String requestId);

}
