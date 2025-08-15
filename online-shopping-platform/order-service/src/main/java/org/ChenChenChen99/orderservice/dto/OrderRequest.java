package org.ChenChenChen99.orderservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class OrderRequest {
    private List<OrderItemRequest> items;
    private BigDecimal totalPrice;

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

}
