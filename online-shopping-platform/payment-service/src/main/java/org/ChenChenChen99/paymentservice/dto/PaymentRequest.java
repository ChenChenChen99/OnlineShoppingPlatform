package org.ChenChenChen99.paymentservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PaymentRequest {
    private UUID orderId;
    private UUID userId;
    private BigDecimal amount;
    private String idempotencyKey;
}
