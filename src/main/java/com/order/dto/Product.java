package com.order.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Product {

    private String productId;

    private String productName;

    private BigDecimal price;

    private BigDecimal gstPercentage;
}
