package com.order.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartResponse {

    private Long cartId;

    private String customerId;

    private String customerName;

    private BigDecimal totalAmount;

    private Integer totalItems;

    private List<CartItemResponse> cartItems;
}
