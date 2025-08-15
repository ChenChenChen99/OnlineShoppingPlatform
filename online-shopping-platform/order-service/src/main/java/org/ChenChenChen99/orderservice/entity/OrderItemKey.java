package org.ChenChenChen99.orderservice.entity;

import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.util.UUID;

@Data
@PrimaryKeyClass
public class OrderItemKey implements Serializable {

    @PrimaryKeyColumn(name = "order_id", type = PrimaryKeyType.PARTITIONED, ordinal = 0)
    private UUID orderId;

    @PrimaryKeyColumn(name = "item_id", type = PrimaryKeyType.CLUSTERED, ordinal = 1)
    private String itemId;

    public OrderItemKey() {}

    public OrderItemKey(UUID orderId, String itemId) {
        this.orderId = orderId;
        this.itemId = itemId;
    }

}