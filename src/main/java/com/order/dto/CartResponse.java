package com.order.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartResponse {

    private Long cartId;

    private String customerId;

    private String customerName;

    private Double totalAmount;

    private Integer totalItems;

    private List<CartItemResponse> cartItems;
}