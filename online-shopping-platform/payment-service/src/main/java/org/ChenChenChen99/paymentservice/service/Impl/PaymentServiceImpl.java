package org.ChenChenChen99.paymentservice.service.Impl;

import jakarta.transaction.Transactional;
import org.ChenChenChen99.paymentservice.entity.Payment;
import org.ChenChenChen99.paymentservice.entity.PaymentStatus;
import org.ChenChenChen99.paymentservice.kafka.PaymentProducer;
import org.ChenChenChen99.paymentservice.repository.PaymentRepository;
import org.ChenChenChen99.paymentservice.service.PaymentService;
import org.ChenChenChen99.kafka.dto.PaymentEvent;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentProducer paymentProducer;

    public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentProducer paymentProducer) {
        this.paymentRepository = paymentRepository;
        this.paymentProducer = paymentProducer;
    }

    @Override
    public Payment submitPayment(UUID orderId, UUID userId, double amount, String idempotencyKey) {
        Optional<Payment> existing = paymentRepository.findByIdempotencyKey(idempotencyKey);
        if (existing.isPresent()) {
            return existing.get();
        }

        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID());
        payment.setOrderId(orderId);
        payment.setUserId(userId);
        payment.setAmount(new java.math.BigDecimal(amount));
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(Instant.now());
        payment.setUpdatedAt(Instant.now());
        payment.setIdempotencyKey(idempotencyKey);

        return paymentRepository.save(payment);
    }

    @Override
    public Payment confirmPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending payments can be confirmed. Current status: " + payment.getStatus()
            );
        }

        payment.setStatus(PaymentStatus.SUCCESS);
        Payment savedPayment = paymentRepository.save(payment);

        PaymentEvent event = new PaymentEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setPaymentId(paymentId.toString());
        event.setOrderId(payment.getOrderId().toString());
        event.setUserId(payment.getUserId().toString());
        event.setAmount(payment.getAmount());
        event.setStatus(PaymentStatus.SUCCESS.name());
        event.setCreatedAt(Instant.now());

        paymentProducer.sendPaymentSuccessful(event);

        return savedPayment;
    }

    @Override
    public Optional<Payment> getPaymentById(UUID paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public Payment refundPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() == PaymentStatus.REFUNDED) {
            return payment;
        }

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new IllegalStateException("Only successful payments can be refunded");
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        Payment savedPayment = paymentRepository.save(payment);

        PaymentEvent event = new PaymentEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setPaymentId(paymentId.toString());
        event.setOrderId(payment.getOrderId().toString());
        event.setUserId(payment.getUserId().toString());
        event.setAmount(payment.getAmount());
        event.setStatus(PaymentStatus.REFUNDED.name());
        event.setCreatedAt(Instant.now());

        paymentProducer.sendPaymentRefunded(event);

        return savedPayment;
    }

    @Override
    public Payment cancelPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending payments can be cancelled. Current status: " + payment.getStatus()
            );
        }

        payment.setStatus(PaymentStatus.CANCELLED);
        payment.setUpdatedAt(Instant.now());
        Payment savedPayment = paymentRepository.save(payment);

        PaymentEvent event = new PaymentEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setPaymentId(paymentId.toString());
        event.setOrderId(payment.getOrderId().toString());
        event.setUserId(payment.getUserId().toString());
        event.setAmount(payment.getAmount());
        event.setStatus(PaymentStatus.CANCELLED.name());
        event.setCreatedAt(Instant.now());

        paymentProducer.sendPaymentCancelled(event);

        return savedPayment;
    }
}