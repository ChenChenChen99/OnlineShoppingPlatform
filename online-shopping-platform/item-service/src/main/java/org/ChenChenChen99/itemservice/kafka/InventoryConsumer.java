package org.ChenChenChen99.itemservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ChenChenChen99.itemservice.service.InventoryService;
import org.ChenChenChen99.kafka.constants.KafkaTopics;
import org.ChenChenChen99.kafka.dto.ItemEvent;
import org.ChenChenChen99.kafka.dto.OrderEvent;
import org.ChenChenChen99.kafka.dto.InventoryEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryConsumer {

    private final InventoryService inventoryService;
    private final InventoryProducer inventoryProducer;

    @KafkaListener(topics = KafkaTopics.ORDER_PAID, groupId = "inventory-service")
    public void handleOrderPaid(OrderEvent event) {
        log.info("Received ORDER_PAID event: {}", event);
        boolean success = true;
        for (ItemEvent item : event.getItems()) {
            try {
                inventoryService.reserveInventory(item.getItemId(), item.getQuantity());
            } catch (RuntimeException e) {
                success = false;
                break;
            }
        }
        InventoryEvent inventoryEvent = new InventoryEvent();
        inventoryEvent.setUserId(event.getUserId());
        inventoryEvent.setOrderId(event.getOrderId());
        inventoryEvent.setItems(event.getItems());
        inventoryEvent.setUpdatedAt(Instant.now());
        inventoryEvent.setStatus(success ? "RESERVED" : "FAILED");

        if (success) {
            inventoryProducer.sendInventoryReserved(inventoryEvent);
        } else {
            inventoryProducer.sendInventoryFailed(inventoryEvent);
        }
    }

    @KafkaListener(topics = KafkaTopics.ORDER_COMPLETED, groupId = "inventory-service")
    public void handleOrderCompleted(OrderEvent event) {
        log.info("Received ORDER_COMPLETED event: {}", event);
        for (ItemEvent item : event.getItems()) {
            String itemId = item.getItemId();
            int quantity = item.getQuantity();
            inventoryService.confirmSale(itemId, quantity);
        }
    }

    @KafkaListener(topics = KafkaTopics.ORDER_CANCELLED, groupId = "inventory-service")
    public void handleOrderCancelled(OrderEvent event) {
        log.info("Received ORDER_CANCELLED event: {}", event);
        for (ItemEvent item : event.getItems()) {
            String itemId = item.getItemId();
            int quantity = item.getQuantity();
            inventoryService.releaseInventory(itemId, quantity);
        }
    }
}