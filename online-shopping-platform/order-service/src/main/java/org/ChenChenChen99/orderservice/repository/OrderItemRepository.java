package org.ChenChenChen99.orderservice.repository;


import org.ChenChenChen99.orderservice.entity.OrderItem;
import org.ChenChenChen99.orderservice.entity.OrderItemKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends CassandraRepository<OrderItem, OrderItemKey> {
    List<OrderItem> findByKeyOrderId(UUID orderId);
}