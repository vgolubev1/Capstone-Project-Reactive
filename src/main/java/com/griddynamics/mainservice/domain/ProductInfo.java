package com.griddynamics.mainservice.domain;

import lombok.Data;

@Data
public class ProductInfo {
    private String productId;
    private String productCode;
    private String productName;
    private double score;

    public ProductInfo() {
    }

    public ProductInfo(String productId, String productCode, String productName, double score) {
        this.productId = productId;
        this.productCode = productCode;
        this.productName = productName;
        this.score = score;
    }
}
