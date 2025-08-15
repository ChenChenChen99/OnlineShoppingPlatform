package org.ChenChenChen99.orderservice.kafka;

import lombok.RequiredArgsConstructor;
import org.ChenChenChen99.kafka.constants.KafkaTopics;
import org.ChenChenChen99.kafka.dto.OrderEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendOrderCreated(OrderEvent event) {
        kafkaTemplate.send(KafkaTopics.ORDER_CREATED, event.getOrderId().toString(), event);
        System.out.println("Sent order_created event: " + event);
    }

    public void sendOrderPaid(OrderEvent event) {
        kafkaTemplate.send(KafkaTopics.ORDER_PAID, event.getOrderId().toString(), event);
        System.out.println("Sent order_paid event: " + event);
    }

    public void sendOrderCompleted(OrderEvent event) {
        kafkaTemplate.send(KafkaTopics.ORDER_COMPLETED, event.getOrderId().toString(), event);
        System.out.println("Sent order_completed event: " + event);
    }

    public void sendOrderCancelled(OrderEvent event) {
        kafkaTemplate.send(KafkaTopics.ORDER_CANCELLED, event.getOrderId().toString(), event);
        System.out.println("Sent order_cancelled event: " + event);
    }
}
