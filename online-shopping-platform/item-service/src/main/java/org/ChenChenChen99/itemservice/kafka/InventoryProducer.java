package org.ChenChenChen99.itemservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ChenChenChen99.kafka.constants.KafkaTopics;
import org.ChenChenChen99.kafka.dto.InventoryEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendInventoryReserved(InventoryEvent event) {
        log.info("Sending INVENTORY_RESERVED event to Kafka: {}", event);
        kafkaTemplate.send(KafkaTopics.INVENTORY_RESERVED, event.getOrderId(), event);
    }

    public void sendInventoryFailed(InventoryEvent event) {
        log.info("Sending INVENTORY_FAILED event to Kafka: {}", event);
        kafkaTemplate.send(KafkaTopics.INVENTORY_FAILED, event.getOrderId(), event);
    }
}