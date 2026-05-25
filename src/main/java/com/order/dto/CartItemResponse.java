package com.order.dto;

import lombok.Data;

@Data
public class CartItemResponse {

    private Long cartItemId;

    private String productId;

    private String productName;

    private Integer quantity;

    private Double unitPrice;

    private Double totalPrice;
}