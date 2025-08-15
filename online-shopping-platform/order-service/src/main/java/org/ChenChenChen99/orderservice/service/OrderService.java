package org.ChenChenChen99.orderservice.service;

import org.ChenChenChen99.orderservice.entity.Order;
import org.ChenChenChen99.orderservice.entity.OrderItem;
import org.ChenChenChen99.orderservice.entity.OrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderService {

    Order createOrder(UUID userId, Order order, List<OrderItem> items);

    Optional<Order> getOrderById(UUID userId, UUID orderId);

    Order updateOrderStatus(UUID userId, UUID orderId, OrderStatus newStatus);

    void cancelOrder(UUID userId, UUID orderId);

    Order markOrderPaid(UUID userId, UUID orderId);

    Order markOrderProcessing(UUID userId, UUID orderId);

    Order completeOrder(UUID userId, UUID orderId);
}