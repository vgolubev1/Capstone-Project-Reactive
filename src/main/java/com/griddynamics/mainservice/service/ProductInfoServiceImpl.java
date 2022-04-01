package com.griddynamics.mainservice.service;

import com.griddynamics.mainservice.Util.Logger;
import com.griddynamics.mainservice.dao.ProductInfoServiceDAO;
import com.griddynamics.mainservice.domain.ProductInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ProductInfoServiceImpl implements ProductInfoService {

    @Autowired
    private ProductInfoServiceDAO productInfoServiceDAO;

    @Override
    public Flux<ProductInfo> getByProductCode(String productCode, String requestId) {

        return productInfoServiceDAO.getByProductCode(productCode, requestId);
    }

    public Mono<ProductInfo> getMostRelevant(Flux<ProductInfo> productInfoFlux, String requestId) {
        return productInfoFlux.reduce((o1, o2) -> o1.getScore() > o2.getScore() ? o1 : o2)
                .doOnEach(Logger.logOnNext(v ->
                        log.info("RequestID[" + requestId + "]: Found mostRelevant: {}", v)))
                .doOnEach(Logger
                        .logOnError(e ->
                                log.error("RequestID[" + requestId + "]: Product info find most relevant error - " + e.getMessage())));
    }

}
