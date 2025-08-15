package org.ChenChenChen99.paymentservice.controller;

import org.ChenChenChen99.paymentservice.dto.PaymentRequest;
import org.ChenChenChen99.paymentservice.entity.Payment;
import org.ChenChenChen99.paymentservice.entity.PaymentStatus;
import org.ChenChenChen99.paymentservice.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Payment> submitPayment(@RequestBody PaymentRequest request) {
        Payment payment = paymentService.submitPayment(
                request.getOrderId(),
                request.getUserId(),
                request.getAmount().doubleValue(),
                request.getIdempotencyKey()
        );

        return ResponseEntity
                .created(URI.create("/payments/" + payment.getPaymentId()))
                .body(payment);
    }

    @PostMapping("/{paymentId}/confirm")
    public ResponseEntity<Payment> confirmPayment(@PathVariable UUID paymentId) {
        Payment payment = paymentService.confirmPayment(paymentId);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getPayment(@PathVariable UUID paymentId) {
        Optional<Payment> payment = paymentService.getPaymentById(paymentId);
        return payment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{paymentId}")
    public ResponseEntity<Payment> updatePaymentStatus(
            @PathVariable UUID paymentId,
            @RequestParam PaymentStatus newStatus) {

        Payment updated;
        switch (newStatus) {
            case SUCCESS:
                updated = paymentService.confirmPayment(paymentId);
                break;
            case REFUNDED:
                updated = paymentService.refundPayment(paymentId);
                break;
            case CANCELLED:
                updated = paymentService.cancelPayment(paymentId);
                break;
            default:
                throw new IllegalArgumentException("Unsupported payment status: " + newStatus);
        }
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<Payment> refundPayment(@PathVariable UUID paymentId) {
        Payment refunded = paymentService.refundPayment(paymentId);
        return ResponseEntity.ok(refunded);
    }
}
