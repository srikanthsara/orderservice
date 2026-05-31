package com.order.service;

import com.common.event.OrderCreatedEvent;
import com.order.client.CartServiceClient;
import com.order.client.GrocerySearchClient;
import com.order.config.OrderProperties;
import com.order.dto.CartItemResponse;
import com.order.dto.CartResponse;
import com.order.dto.CheckoutRequest;
import com.order.dto.Product;
import com.order.entity.OrderItem;
import com.order.entity.OrderMaster;
import com.order.kafka.OrderEventProducer;
import com.order.repository.OrderMasterRepository;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMasterRepository repository;
    private final CartServiceClient cartClient;
    private final OrderEventProducer producer;
    private final OrderProperties properties;
    private final GrocerySearchClient grocerySearchClient;

    @Transactional
    public OrderMaster checkout(CheckoutRequest request) {

        // FETCH CUSTOMER CART
        CartResponse cart = cartClient.getCart(request.getCustomerId());
        // SUBTOTAL
        BigDecimal subTotal = cart.getTotalAmount();

        BigDecimal totalGST = BigDecimal.ZERO;

        for (CartItemResponse cartItem : cart.getCartItems()) {

            Product product =
                    grocerySearchClient.getProductById(cartItem.getProductId());

            BigDecimal itemTotal =  cartItem.getTotalPrice();
            //BigDecimal gstPercentage = product.getGstPercentage();

            BigDecimal gstPercentage =
                    product.getGstPercentage() == null
                            ? BigDecimal.ZERO
                            : product.getGstPercentage();

            BigDecimal itemGST =
                    itemTotal
                            .multiply(gstPercentage)
                            .divide(
                                    BigDecimal.valueOf(100),
                                    2,
                                    RoundingMode.HALF_UP);

            totalGST = totalGST.add(itemGST);
        }


        BigDecimal shipping = properties.getShipping().getCharge();
        BigDecimal discount = properties.getDiscount().getDefaultDiscount();

        BigDecimal total = subTotal
                            .add(totalGST)
                            .add(shipping)
                            .subtract(discount);

        // CREATE ORDER
        OrderMaster order = OrderMaster.builder()
                            .customerId(cart.getCustomerId())
                            .orderItems(new ArrayList<>())
                            .customerName(cart.getCustomerName())
                            .customerEmail(request.getCustomerEmail())
                            .subTotal(subTotal)
                            .gstAmount(totalGST)
                            .shippingCharges(shipping)
                            .discountAmount(discount)
                            .totalAmount(total)
                            .paymentStatus(
                                    properties.getStatus()
                                            .getPaymentPending())

                            .inventoryStatus(
                                    properties.getStatus()
                                            .getInventoryPending())

                            .orderStatus(
                                    properties.getStatus()
                                            .getOrderCreated())

                            .createdAt(LocalDateTime.now())
                            .build();
        order.setOrderItems(new ArrayList<>());
        // CREATE ORDER ITEMS
        for (CartItemResponse cartItem : cart.getCartItems()) {

            Product product =
                    grocerySearchClient.getProductById(
                            cartItem.getProductId());

            BigDecimal itemTotal = cartItem.getTotalPrice();

            BigDecimal itemGST =
                    itemTotal
                            .multiply(product.getGstPercentage())
                            .divide(
                                    BigDecimal.valueOf(100),
                                    2,
                                    RoundingMode.HALF_UP);

            OrderItem item = OrderItem.builder()

                    .productId(cartItem.getProductId())

                    .productName(cartItem.getProductName())

                    .quantity(cartItem.getQuantity())

                    .unitPrice(cartItem.getUnitPrice())

                    .totalPrice(cartItem.getTotalPrice())

                    .gstPercentage(product.getGstPercentage())

                    .gstAmount(itemGST)

                    .orderMaster(order)

                    .build();

            order.getOrderItems().add(item);
        }

        // SAVE ORDER
        OrderMaster savedOrder = repository.saveAndFlush(order);

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
        cartClient.clearCart(request.getCustomerId());
        return savedOrder;
    }
}