package com.order.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_master")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private String customerId;

    private String customerName;

    private String customerEmail;

    private BigDecimal subTotal;

    private BigDecimal gstAmount;

    private BigDecimal shippingCharges;

    private BigDecimal discountAmount;

    private BigDecimal totalAmount;

    private String paymentStatus;

    private String inventoryStatus;

    private String orderStatus;

    private LocalDateTime createdAt;

    @OneToMany(
            mappedBy = "orderMaster",

            cascade = CascadeType.ALL
    )

    @JsonManagedReference

    @Builder.Default
    private List<OrderItem> orderItems =
            new ArrayList<>();
}