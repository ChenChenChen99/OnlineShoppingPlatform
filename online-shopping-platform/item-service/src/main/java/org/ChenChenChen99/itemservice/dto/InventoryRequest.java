package org.ChenChenChen99.itemservice.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class InventoryRequest {
    private String itemId;
    private int quantity;
}
