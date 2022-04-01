package com.griddynamics.mainservice.domain;

import lombok.Data;

@Data
public class ProductInfo {
    private String productId;
    private String productCode;
    private String productName;
    private double score;
}
