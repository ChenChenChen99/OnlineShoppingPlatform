package org.ChenChenChen99.accountservice.entity;

import jakarta.persistence.*;
import lombok.Builder;

import java.util.UUID;

@Entity
@Table(name = "users")
@Builder
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    private UUID userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column
    private String shippingAddress;

    @Column
    private String billingAddress;

    @Column
    private String paymentMethod;

    public User() {
    }

    public User(UUID userId, String email, String username, String passwordHash,
                String shippingAddress, String billingAddress, String paymentMethod) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.passwordHash = passwordHash;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.paymentMethod = paymentMethod;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail(){
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getShippingAddress(){
        return shippingAddress;
    }

    public String getBillingAddress(){
        return billingAddress;
    }

    public String getPaymentMethod(){
        return paymentMethod;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String encode) {
        this.passwordHash = encode;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

}
