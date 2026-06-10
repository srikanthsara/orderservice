package com.order.kafka;

import com.common.event.PaymentFailedEvent;
import com.common.event.PaymentSuccessEvent;
import com.order.entity.OrderMaster;
import com.order.repository.OrderMasterRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentStatusConsumer {

    private final OrderMasterRepository  repository;

    @KafkaListener(
            topics = "payment-success-topic",

            groupId = "order-payment-group"
    )
    public void consumeSuccess(
            PaymentSuccessEvent event) {

        System.out.println(
                "PAYMENT SUCCESS RECEIVED: "
                        + event);

        OrderMaster order =
                repository.findById(
                                event.getOrderId())

                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Order Not Found"));

        order.setPaymentStatus("SUCCESS");

        order.setOrderStatus(
                "PAYMENT_COMPLETED");

        repository.save(order);

        System.out.println(
                "ORDER UPDATED SUCCESSFULLY");
    }

    @KafkaListener(
            topics = "payment-failed-topic",

            groupId = "order-payment-group"
    )
    public void consumeFailure(
            PaymentFailedEvent event) {

        System.out.println(
                "PAYMENT FAILED RECEIVED");

        OrderMaster order =
                repository.findById(
                                event.getOrderId())

                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Order Not Found"));

        order.setPaymentStatus("FAILED");

        order.setOrderStatus(
                "PAYMENT_FAILED");

        repository.save(order);
    }
}
