package org.ChenChenChen99.accountservice.dto;

import lombok.Data;

@Data
public class UpdateRequest {
    private String username;
    private String email;
    private String shippingAddress;
    private String billingAddress;
    private String paymentMethod;
}