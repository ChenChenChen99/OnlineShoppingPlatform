package org.ChenChenChen99.orderservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ChenChenChen99.kafka.constants.KafkaTopics;
import org.ChenChenChen99.kafka.dto.OrderEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderProducer {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendOrderCreated(OrderEvent event) {
        log.info("Sending ORDER_CREATED event: {}", event);
        kafkaTemplate.send(KafkaTopics.ORDER_CREATED, event.getOrderId().toString(), event);
    }

    public void sendOrderPaid(OrderEvent event) {
        log.info("Sending ORDER_PAID event: {}", event);
        kafkaTemplate.send(KafkaTopics.ORDER_PAID, event.getOrderId().toString(), event);
    }

    public void sendOrderCompleted(OrderEvent event) {
        log.info("Sending ORDER_COMPLETED event: {}", event);
        kafkaTemplate.send(KafkaTopics.ORDER_COMPLETED, event.getOrderId().toString(), event);
    }

    public void sendOrderCancelled(OrderEvent event) {
        log.info("Sending ORDER_CANCELLED event: {}", event);
        kafkaTemplate.send(KafkaTopics.ORDER_CANCELLED, event.getOrderId().toString(), event);
    }
}
