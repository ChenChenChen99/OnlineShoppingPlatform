package org.ChenChenChen99.orderservice.repository;

import org.ChenChenChen99.orderservice.entity.Order;
import org.ChenChenChen99.orderservice.entity.OrderPrimaryKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CassandraRepository<Order, OrderPrimaryKey> {
}