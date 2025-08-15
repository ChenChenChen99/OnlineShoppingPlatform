package org.ChenChenChen99.paymentservice.kafka;

import lombok.RequiredArgsConstructor;
import org.ChenChenChen99.kafka.constants.KafkaTopics;
import org.ChenChenChen99.kafka.dto.OrderEvent;
import org.ChenChenChen99.paymentservice.entity.Payment;
import org.ChenChenChen99.paymentservice.repository.PaymentRepository;
import org.ChenChenChen99.paymentservice.service.PaymentService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentConsumer {

    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    @KafkaListener(topics = KafkaTopics.ORDER_CREATED, groupId = "payment-service")
    public void handleOrderCreated(OrderEvent event) {
        paymentService.submitPayment(
                java.util.UUID.fromString(event.getOrderId().toString()),
                java.util.UUID.fromString(event.getUserId().toString()),
                event.getTotalAmount().doubleValue(),
                event.getIdempotencyKey()
        );
    }

    @KafkaListener(topics = KafkaTopics.ORDER_CANCELLED, groupId = "payment-service")
    public void handleOrderCancelled(OrderEvent event) {
        UUID orderId = UUID.fromString(event.getOrderId());
        Optional<Payment> paymentOpt = paymentRepository.findByOrderId(orderId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            paymentService.cancelPayment(payment.getPaymentId());
        } else {
            System.out.println("No payment found for orderId: " + orderId);
        }
    }
}