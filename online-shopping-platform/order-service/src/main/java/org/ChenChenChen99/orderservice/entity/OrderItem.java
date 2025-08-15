package org.ChenChenChen99.orderservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.math.BigDecimal;

@Table("order_items_by_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    @PrimaryKey
    private OrderItemKey key;

    @Column("qty")
    private int qty;

    @Column("price")
    private BigDecimal price;
}
