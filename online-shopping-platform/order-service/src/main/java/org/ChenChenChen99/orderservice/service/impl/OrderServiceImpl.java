package org.ChenChenChen99.orderservice.service.impl;

import org.ChenChenChen99.kafka.dto.ItemEvent;
import org.ChenChenChen99.kafka.dto.OrderEvent;
import org.ChenChenChen99.orderservice.entity.*;
import org.ChenChenChen99.orderservice.kafka.OrderProducer;
import org.ChenChenChen99.orderservice.repository.OrderRepository;
import org.ChenChenChen99.orderservice.repository.OrderItemRepository;
import org.ChenChenChen99.orderservice.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderProducer orderProducer;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, OrderProducer orderProducer) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderProducer = orderProducer;
    }

    @Override
    public Order createOrder(UUID userId, Order order, List<OrderItem> items) {
        UUID orderId = UUID.randomUUID();

        order.setKey(new OrderPrimaryKey(userId, orderId));
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(Instant.now());
        order.setUpdatedAt(Instant.now());
        orderRepository.save(order);

        for (OrderItem item : items) {
            item.getKey().setOrderId(orderId);
            orderItemRepository.save(item);
        }

        OrderEvent event = new OrderEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setOrderId(orderId.toString());
        event.setUserId(userId.toString());
        event.setTotalAmount(order.getTotalPrice());
        event.setStatus(order.getStatus().name());
        event.setCreatedAt(order.getCreatedAt());
        event.setIdempotencyKey(UUID.randomUUID().toString());

        List<ItemEvent> itemEvents = items.stream().map(i -> {
            ItemEvent ie = new ItemEvent();
            ie.setItemId(i.getKey().getItemId());
            ie.setQuantity(i.getQty());
            return ie;
        }).toList();
        event.setItems(itemEvents);

        orderProducer.sendOrderCreated(event);

        return order;
    }

    @Override
    public Optional<Order> getOrderById(UUID userId, UUID orderId) {
        return orderRepository.findById(new OrderPrimaryKey(userId, orderId));
    }

    @Override
    public Order updateOrderStatus(UUID userId, UUID orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(new OrderPrimaryKey(userId, orderId))
                .orElseThrow(() -> new RuntimeException("Order not found"));


        order.setStatus(newStatus);
        order.setUpdatedAt(Instant.now());
        Order savedOrder = orderRepository.save(order);

        if (newStatus == OrderStatus.PAID) {
            OrderEvent event = new OrderEvent();
            event.setEventId(UUID.randomUUID().toString());
            event.setIdempotencyKey("order_" + orderId + "_paid");
            event.setOrderId(orderId.toString());
            event.setUserId(userId.toString());
            event.setStatus(OrderStatus.PAID.name());
            event.setCreatedAt(Instant.now());
            event.setTotalAmount(order.getTotalPrice());

            List<OrderItem> items = orderItemRepository.findByKeyOrderId(orderId);
            List<ItemEvent> itemEvents = items.stream()
                    .map(i -> new ItemEvent(i.getKey().getItemId(), i.getQty()))
                    .collect(Collectors.toList());
            event.setItems(itemEvents);

            orderProducer.sendOrderPaid(event);
        }
        return savedOrder;
    }

    @Override
    public void cancelOrder(UUID userId, UUID orderId) {
        Order order = orderRepository.findById(new OrderPrimaryKey(userId, orderId))
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.CREATED && order.getStatus() != OrderStatus.PAID) {
            throw new IllegalStateException(
                    "Order cannot be cancelled from status: " + order.getStatus()
            );
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(Instant.now());
        orderRepository.save(order);

        OrderEvent event = new OrderEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setOrderId(orderId.toString());
        event.setUserId(userId.toString());
        event.setStatus(OrderStatus.CANCELLED.name());
        event.setCreatedAt(Instant.now());
        event.setIdempotencyKey(UUID.randomUUID().toString());
        event.setTotalAmount(order.getTotalPrice());

        List<OrderItem> items = orderItemRepository.findByKeyOrderId(orderId);
        List<ItemEvent> itemEvents = items.stream()
                .map(i -> new ItemEvent(i.getKey().getItemId(), i.getQty()))
                .collect(Collectors.toList());
        event.setItems(itemEvents);

        orderProducer.sendOrderCancelled(event);
    }

    @Override
    public Order markOrderPaid(UUID userId, UUID orderId) {
        Order order = orderRepository.findById(new OrderPrimaryKey(userId, orderId))
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new IllegalStateException(
                    "Order must be in CREATED state to mark as PAID. Current: " + order.getStatus()
            );
        }

        order.setStatus(OrderStatus.PAID);
        order.setUpdatedAt(Instant.now());
        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = orderItemRepository.findByKeyOrderId(orderId);

        List<ItemEvent> itemEvents = orderItems.stream()
                .map(oi -> new ItemEvent(
                        oi.getKey().getItemId().toString(),
                        oi.getQty()
                ))
                .collect(Collectors.toList());

        OrderEvent event = new OrderEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setOrderId(orderId.toString());
        event.setUserId(userId.toString());
        event.setStatus(OrderStatus.PAID.name());
        event.setCreatedAt(Instant.now());
        event.setItems(itemEvents);
        event.setTotalAmount(order.getTotalPrice());
        event.setIdempotencyKey("order_" + orderId + "_paid");

        orderProducer.sendOrderPaid(event);

        return savedOrder;
    }

    @Override
    public Order markOrderProcessing(UUID userId, UUID orderId) {
        Order order = orderRepository.findById(new OrderPrimaryKey(userId, orderId))
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.PAID) {
            throw new IllegalStateException(
                    "Order must be in PAID state to mark as PROCESSING. Current: " + order.getStatus()
            );
        }

        order.setStatus(OrderStatus.PROCESSING);
        order.setUpdatedAt(Instant.now());
        Order savedOrder = orderRepository.save(order);

        return savedOrder;
    }

    @Override
    public Order completeOrder(UUID userId, UUID orderId) {
        Order order = orderRepository.findById(new OrderPrimaryKey(userId, orderId))
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.PROCESSING) {
            throw new IllegalStateException(
                    "Order must be in PROCESSING state to complete. Current: " + order.getStatus()
            );
        }

        order.setStatus(OrderStatus.COMPLETED);
        order.setUpdatedAt(Instant.now());
        Order savedOrder = orderRepository.save(order);

        OrderEvent event = new OrderEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setOrderId(orderId.toString());
        event.setUserId(userId.toString());
        event.setStatus(OrderStatus.COMPLETED.name());
        event.setCreatedAt(Instant.now());
        event.setItems(Collections.emptyList());
        event.setTotalAmount(order.getTotalPrice());
        event.setIdempotencyKey("order_" + orderId + "_completed");

        orderProducer.sendOrderCompleted(event);

        return savedOrder;
    }

}
