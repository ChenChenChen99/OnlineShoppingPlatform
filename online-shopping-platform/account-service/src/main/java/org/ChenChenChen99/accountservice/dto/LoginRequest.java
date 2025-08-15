package org.ChenChenChen99.accountservice.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
