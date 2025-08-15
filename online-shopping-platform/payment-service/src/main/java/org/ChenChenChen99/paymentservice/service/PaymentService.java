package org.ChenChenChen99.paymentservice.service;

import org.ChenChenChen99.paymentservice.entity.Payment;
import org.ChenChenChen99.paymentservice.entity.PaymentStatus;

import java.util.Optional;
import java.util.UUID;

public interface PaymentService {

    Optional<Payment> getPaymentById(UUID paymentId);

    Payment submitPayment(UUID orderId, UUID userId, double amount, String idempotencyKey);

    Payment confirmPayment(UUID paymentId);

    Payment refundPayment(UUID paymentId);

    Payment cancelPayment(UUID paymentId);
}