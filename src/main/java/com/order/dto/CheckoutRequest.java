package com.order.dto;

import lombok.Data;

@Data
public class CheckoutRequest {

    private String customerId;

    private String customerName;

    private String customerEmail;
}