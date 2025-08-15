package org.ChenChenChen99.accountservice.dto;

public class LoginResponse {
    private String userId;
    private String token;

    public LoginResponse(String userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }
}
