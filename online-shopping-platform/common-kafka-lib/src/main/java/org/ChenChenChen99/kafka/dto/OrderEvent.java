package org.ChenChenChen99.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {
    private String eventId;
    private String orderId;
    private String userId;
    private List<ItemEvent> items;
    private BigDecimal totalAmount;
    private String status;
    private Instant createdAt;
    private String idempotencyKey;
}
