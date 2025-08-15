package org.ChenChenChen99.orderservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemRequest {
    private String name;
    private int qty;
    private BigDecimal price;
}