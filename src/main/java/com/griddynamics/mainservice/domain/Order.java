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

    public Order(String orderNumber, String userName, String phoneNumber, String productCode, String productName, String productId) {
        this.orderNumber = orderNumber;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.productCode = productCode;
        this.productName = productName;
        this.productId = productId;
    }

    public Order() {
    }
}
