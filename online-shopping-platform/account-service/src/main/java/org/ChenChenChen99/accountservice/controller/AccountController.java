package org.ChenChenChen99.accountservice.controller;

import org.ChenChenChen99.accountservice.dto.*;
import org.ChenChenChen99.accountservice.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        accountService.register(request);
        return ResponseEntity.ok("Account created successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(accountService.login(request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getAccount(@PathVariable UUID userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = auth.getName();

        if (!currentUserId.equals(userId.toString())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized");
        }

        return accountService.getAccount(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateAccount(@PathVariable UUID userId, @RequestBody UpdateRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = auth.getName();

        if (!currentUserId.equals(userId.toString())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized");
        }

        try {
            accountService.updateAccount(userId, request);
            return ResponseEntity.ok("Account updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

