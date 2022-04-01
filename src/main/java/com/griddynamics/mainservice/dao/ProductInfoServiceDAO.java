package com.griddynamics.mainservice.dao;

import com.griddynamics.mainservice.domain.ProductInfo;
import reactor.core.publisher.Flux;

public interface ProductInfoServiceDAO {
    Flux<ProductInfo> getByProductCode(String productCode, String requestId);
}
