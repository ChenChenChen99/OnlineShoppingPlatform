package org.ChenChenChen99.itemservice.kafka;

import lombok.RequiredArgsConstructor;
import org.ChenChenChen99.kafka.constants.KafkaTopics;
import org.ChenChenChen99.kafka.dto.InventoryEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
//@RequiredArgsConstructor
public class InventoryProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public InventoryProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendInventoryReserved(InventoryEvent event) {
        kafkaTemplate.send(KafkaTopics.INVENTORY_RESERVED, event.getOrderId(), event);
    }

    public void sendInventoryFailed(InventoryEvent event) {
        kafkaTemplate.send(KafkaTopics.INVENTORY_FAILED, event.getOrderId(), event);
    }
}