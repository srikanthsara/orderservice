package com.order.service;

import com.common.event.OrderCreatedEvent;
import com.common.services.PriceCalculationService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMasterRepository repository;
    private final CartServiceClient cartClient;
    private final OrderEventProducer producer;
    private final OrderProperties properties;
    private final GrocerySearchClient grocerySearchClient;
    private final PriceCalculationService priceCalculationService;

    @Transactional
    public OrderMaster checkout(CheckoutRequest request) {

        // FETCH CUSTOMER CART
        CartResponse cart =
                cartClient.getCart(
                        request.getCustomerId());

        if (cart == null ||
                cart.getCartItems() == null ||
                cart.getCartItems().isEmpty()) {

            throw new RuntimeException(
                    "Cart is Empty");
        }

        // TOTAL GST FROM CART ITEMS
        BigDecimal totalGST =

                cart.getCartItems()

                        .stream()

                        .map(item ->

                                item.getGstAmount() == null

                                        ? BigDecimal.ZERO

                                        : item.getGstAmount()
                        )

                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        // SUBTOTAL (WITHOUT GST)
        BigDecimal subTotal =

                cart.getTotalAmount()
                        .subtract(totalGST);

        BigDecimal shipping =
                properties.getShipping()
                        .getCharge();

        BigDecimal discount =
                properties.getDiscount()
                        .getDefaultDiscount();

        // FINAL TOTAL
        BigDecimal total =

                priceCalculationService
                        .calculateOrderTotal(
                                subTotal,
                                totalGST,
                                shipping,
                                discount
                        );

        System.out.println(
                "SubTotal = " + subTotal);

        System.out.println(
                "GST = " + totalGST);

        System.out.println(
                "Shipping = " + shipping);

        System.out.println(
                "Discount = " + discount);

        System.out.println(
                "Final Total = " + total);

        // CREATE ORDER
        OrderMaster order =

                OrderMaster.builder()

                        .customerId(
                                cart.getCustomerId())

                        .customerName(
                                cart.getCustomerName())

                        .customerEmail(
                                request.getCustomerEmail())

                        .subTotal(
                                subTotal)

                        .gstAmount(
                                totalGST)

                        .shippingCharges(
                                shipping)

                        .discountAmount(
                                discount)

                        .totalAmount(
                                total)

                        .paymentStatus(
                                properties.getStatus()
                                        .getPaymentPending())

                        .inventoryStatus(
                                properties.getStatus()
                                        .getInventoryPending())

                        .orderStatus(
                                properties.getStatus()
                                        .getOrderCreated())

                        .createdAt(
                                LocalDateTime.now())

                        .orderItems(
                                new ArrayList<>())

                        .build();

        // CREATE ORDER ITEMS
        for (CartItemResponse cartItem :
                cart.getCartItems()) {

            Product product =
                    grocerySearchClient
                            .getProductById(
                                    cartItem.getProductId());

            OrderItem item =

                    OrderItem.builder()

                            .productId(
                                    cartItem.getProductId())

                            .productName(
                                    cartItem.getProductName())

                            .quantity(
                                    cartItem.getQuantity())

                            .unitPrice(
                                    cartItem.getUnitPrice())

                            .totalPrice(
                                    cartItem.getTotalPrice())

                            .gstPercentage(
                                    product.getGstPercentage())

                            .gstAmount(
                                    cartItem.getGstAmount())

                            .orderMaster(
                                    order)

                            .build();

            order.getOrderItems()
                    .add(item);
        }

        // SAVE ORDER
        OrderMaster savedOrder =
                repository.saveAndFlush(order);

        // PUBLISH EVENT
        OrderCreatedEvent event =

                OrderCreatedEvent.builder()

                        .orderId(
                                savedOrder.getOrderId())

                        .customerId(
                                savedOrder.getCustomerId())

                        .totalAmount(
                                savedOrder.getTotalAmount())

                        .paymentProvider(
                                request.getPaymentProvider())

                        .paymentType(
                                request.getPaymentType())

                        .orderStatus(
                                savedOrder.getOrderStatus())

                        .createdAt(
                                LocalDateTime.now())

                        .build();

        producer.publishOrderCreatedEvent(
                event);

        // CLEAR CART
        cartClient.clearCart(
                request.getCustomerId());

        return savedOrder;
    }

    public List<OrderMaster> getOrdersByCustomerId(String customerId) {

        return repository.findByCustomerId(customerId);
    }

    public Page<OrderMaster> getOrders(
            String customerId,
            int page,
            int size) {

        return repository
                .findByCustomerIdOrderByOrderIdDesc(
                        customerId,
                        PageRequest.of(page, size));
    }
}
