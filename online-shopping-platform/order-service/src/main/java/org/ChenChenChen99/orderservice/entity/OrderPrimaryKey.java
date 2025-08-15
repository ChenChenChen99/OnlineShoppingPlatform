package org.ChenChenChen99.orderservice.entity;

import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;

import java.io.Serializable;
import java.util.UUID;

@PrimaryKeyClass
public class OrderPrimaryKey implements Serializable {

    @PrimaryKeyColumn(name = "user_id", type = PrimaryKeyType.PARTITIONED, ordinal = 0)
    private UUID userId;

    @PrimaryKeyColumn(name = "order_id", type = PrimaryKeyType.CLUSTERED, ordinal = 1)
    private UUID orderId;

    public OrderPrimaryKey() {}

    public OrderPrimaryKey(UUID userId, UUID orderId) {
        this.userId = userId;
        this.orderId = orderId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderPrimaryKey that = (OrderPrimaryKey) o;

        if (!userId.equals(that.userId)) return false;
        return orderId.equals(that.orderId);
    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + orderId.hashCode();
        return result;
    }
}
