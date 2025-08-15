package org.ChenChenChen99.accountservice.service;

import org.ChenChenChen99.accountservice.dto.*;
import org.ChenChenChen99.accountservice.entity.User;
import org.ChenChenChen99.accountservice.repository.UserRepository;
import org.ChenChenChen99.security.util.JwtTokenUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    public AccountService(UserRepository userRepository, JwtTokenUtil jwtTokenUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .shippingAddress(request.getShippingAddress())
                .billingAddress(request.getBillingAddress())
                .paymentMethod(request.getPaymentMethod())
                .build();

        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtTokenUtil.generateToken(user.getUserId().toString(), List.of("USER"));

        return new LoginResponse(user.getUserId().toString(), token);
    }

    public Optional<User> getAccount(UUID userId) {
        return userRepository.findById(userId);
    }

    public void updateAccount(UUID userId, UpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getUsername() != null) user.setUsername(request.getUsername());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getShippingAddress() != null) user.setShippingAddress(request.getShippingAddress());
        if (request.getBillingAddress() != null) user.setBillingAddress(request.getBillingAddress());
        if (request.getPaymentMethod() != null) user.setPaymentMethod(request.getPaymentMethod());

        userRepository.save(user);
    }
}
