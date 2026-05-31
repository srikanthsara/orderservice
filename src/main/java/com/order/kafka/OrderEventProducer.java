package com.order.kafka;


import com.common.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public void publishOrderCreatedEvent(
            OrderCreatedEvent event) {

        kafkaTemplate.send(
                "order-created-topic",
                event);
    }
}
