package org.ChenChenChen99.paymentservice.kafka;

import lombok.RequiredArgsConstructor;
import org.ChenChenChen99.kafka.constants.KafkaTopics;
import org.ChenChenChen99.kafka.dto.PaymentEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPaymentSuccessful(PaymentEvent event) {
        kafkaTemplate.send(KafkaTopics.PAYMENT_SUCCESSFUL, event.getOrderId(), event);
    }

    public void sendPaymentCancelled(PaymentEvent event) {
        kafkaTemplate.send(KafkaTopics.PAYMENT_CANCELLED, event.getOrderId(), event);
    }

    public void sendPaymentRefunded(PaymentEvent event) {
        kafkaTemplate.send(KafkaTopics.PAYMENT_REFUNDED, event.getOrderId(), event);
    }
}
