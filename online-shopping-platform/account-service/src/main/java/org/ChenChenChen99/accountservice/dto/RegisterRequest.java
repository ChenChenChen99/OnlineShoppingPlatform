package org.ChenChenChen99.accountservice.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String username;
    private String password;
    private String shippingAddress;
    private String billingAddress;
    private String paymentMethod;
}