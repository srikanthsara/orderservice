package com.order.service;

import com.order.client.CartServiceClient;
import com.order.dto.CartItemResponse;
import com.order.dto.CartResponse;
import com.order.dto.CheckoutRequest;
import com.order.entity.OrderItem;
import com.order.entity.OrderMaster;
import com.order.event.OrderCreatedEvent;
import com.order.kafka.OrderEventProducer;
import com.order.repository.OrderMasterRepository;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMasterRepository repository;

    private final CartServiceClient cartClient;

    private final OrderEventProducer producer;

    @Transactional
    public OrderMaster checkout(
            CheckoutRequest request) {

        // FETCH CUSTOMER CART
        CartResponse cart = cartClient

                .getCart(request.getCustomerId());

        // SUBTOTAL
        double subTotal = cart.getTotalAmount();

        // GST
        double gst = subTotal * 0.18;

        // SHIPPING
        double shipping = 100;

        // DISCOUNT
        double discount = 200;

        // FINAL TOTAL
        double total =
                subTotal
                        + gst
                        + shipping
                        - discount;

        // CREATE ORDER
        OrderMaster order = OrderMaster.builder()

                .customerId(cart.getCustomerId())

                .customerName(cart.getCustomerName())

                .customerEmail(request.getCustomerEmail())

                .subTotal(subTotal)

                .gstAmount(gst)

                .shippingCharges(shipping)

                .discountAmount(discount)

                .totalAmount(total)

                .paymentStatus("PENDING")

                .inventoryStatus("PENDING")

                .orderStatus("ORDER_CREATED")

                .createdAt(LocalDateTime.now())

                .build();

        // CREATE ORDER ITEMS
        for (CartItemResponse cartItem
                : cart.getCartItems()) {

            OrderItem item = OrderItem.builder()

                    .productId(cartItem.getProductId())

                    .productName(cartItem.getProductName())

                    .quantity(cartItem.getQuantity())

                    .unitPrice(cartItem.getUnitPrice())

                    .totalPrice(cartItem.getTotalPrice())

                    .orderMaster(order)

                    .build();

            order.getOrderItems().add(item);
        }

        // SAVE ORDER
        OrderMaster savedOrder =
                repository.save(order);

        // PUBLISH EVENT
        OrderCreatedEvent event =

                OrderCreatedEvent.builder()

                        .orderId(savedOrder.getOrderId())

                        .customerId(savedOrder.getCustomerId())

                        .totalAmount(savedOrder.getTotalAmount())

                        .orderStatus(savedOrder.getOrderStatus())

                        .createdAt(LocalDateTime.now())

                        .build();

        producer.publishOrderCreatedEvent(event);

        // CLEAR CUSTOMER CART
        cartClient.clearCart(
                request.getCustomerId());

        return savedOrder;
    }
}