package org.ChenChenChen99.orderservice.kafka;

import lombok.RequiredArgsConstructor;
import org.ChenChenChen99.kafka.constants.KafkaTopics;
import org.ChenChenChen99.kafka.dto.InventoryEvent;
import org.ChenChenChen99.kafka.dto.PaymentEvent;
import org.ChenChenChen99.orderservice.service.OrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderConsumer {
    private final OrderService orderService;
    private final OrderProducer orderProducer;

    @KafkaListener(topics = KafkaTopics.PAYMENT_SUCCESSFUL, groupId = "order-service")
    public void handlePaymentSuccessful(PaymentEvent event) {
        UUID orderId = UUID.fromString(event.getOrderId());
        UUID userId = UUID.fromString(event.getUserId());
        orderService.markOrderPaid(userId, orderId);
    }

    @KafkaListener(topics = KafkaTopics.PAYMENT_CANCELLED, groupId = "order-service")
    public void handlePaymentCancelled(PaymentEvent event) {
        UUID orderId = UUID.fromString(event.getOrderId());
        UUID userId = UUID.fromString(event.getUserId());
        orderService.cancelOrder(userId, orderId);
    }

    @KafkaListener(topics = KafkaTopics.PAYMENT_REFUNDED, groupId = "order-service")
    public void handlePaymentRefunded(PaymentEvent event) {
        UUID orderId = UUID.fromString(event.getOrderId());
        UUID userId = UUID.fromString(event.getUserId());
        orderService.cancelOrder(userId, orderId);
    }

    @KafkaListener(topics = KafkaTopics.INVENTORY_RESERVED, groupId = "order-service")
    public void handleInventoryReserved(InventoryEvent event) {
        UUID orderId = UUID.fromString(event.getOrderId());
        UUID userId = UUID.fromString(event.getUserId());
        orderService.markOrderProcessing(userId, orderId);
    }

    @KafkaListener(topics = KafkaTopics.INVENTORY_FAILED, groupId = "order-service")
    public void handleInventoryFailed(InventoryEvent event) {
        UUID orderId = UUID.fromString(event.getOrderId());
        UUID userId = UUID.fromString(event.getUserId());
        orderService.cancelOrder(userId, orderId);
    }
}
