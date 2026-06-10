package com.order.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private List<OrderItem> orderItems =  new ArrayList<>();
}
