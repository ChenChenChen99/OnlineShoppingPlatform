package org.ChenChenChen99.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEvent {
    private String eventId;
    private String orderId;
    private String paymentId;
    private String userId;
    private BigDecimal amount;
    private String status;
    private Instant createdAt;
}