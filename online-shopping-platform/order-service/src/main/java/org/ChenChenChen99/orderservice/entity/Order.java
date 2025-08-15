package org.ChenChenChen99.orderservice.entity;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Table("orders_by_id")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @PrimaryKey
    private OrderPrimaryKey key;

    private OrderStatus status;

    @Column("total_price")
    private BigDecimal totalPrice;

    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
    private Instant updatedAt;

}