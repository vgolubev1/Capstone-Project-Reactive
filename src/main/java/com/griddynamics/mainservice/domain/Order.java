package com.griddynamics.mainservice.domain;

import lombok.Data;

@Data
public class Order {
    private String orderNumber;
    private String userName;
    private String phoneNumber;
    private String productCode;
    private String productName;
    private String productId;
}
