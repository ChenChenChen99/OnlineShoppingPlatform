package org.ChenChenChen99.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryEvent {
    private String userId;
    private String orderId;
    private List<ItemEvent> items;
    private String status;
    private Instant updatedAt;
}