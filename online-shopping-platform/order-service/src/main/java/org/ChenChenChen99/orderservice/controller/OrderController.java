package org.ChenChenChen99.orderservice.controller;

import org.ChenChenChen99.orderservice.dto.OrderRequest;
import org.ChenChenChen99.orderservice.entity.Order;
import org.ChenChenChen99.orderservice.entity.OrderItem;
import org.ChenChenChen99.orderservice.entity.OrderItemKey;
import org.ChenChenChen99.orderservice.entity.OrderStatus;
import org.ChenChenChen99.orderservice.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestParam UUID userId, @RequestBody OrderRequest request) {
        Order order = new Order();
        order.setTotalPrice(request.getTotalPrice());

        List<OrderItem> items = request.getItems().stream()
                .map(i -> {
                    OrderItem item = new OrderItem();
                    item.setKey(new OrderItemKey(null, UUID.fromString(i.getItemId()))); // orderId 先留 null
                    item.setQty(i.getQty());
                    item.setPrice(i.getPrice());
                    return item;
                })
                .toList();

        Order savedOrder = orderService.createOrder(userId, order, items);

        return ResponseEntity
                .created(URI.create("/orders/" + savedOrder.getKey().getOrderId()))
                .body(savedOrder);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@RequestParam UUID userId, @PathVariable UUID orderId) {
        Optional<Order> order = orderService.getOrderById(userId, orderId);
        return order.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrderStatus(
            @RequestParam UUID userId,
            @PathVariable UUID orderId,
            @RequestParam OrderStatus newStatus) {

        Order updatedOrder = orderService.updateOrderStatus(userId, orderId, newStatus);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(
            @RequestParam UUID userId,
            @PathVariable UUID orderId) {

        orderService.cancelOrder(userId, orderId);
        return ResponseEntity.noContent().build();
    }

}
