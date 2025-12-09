package com.spencer.payments.controller;

import com.spencer.payments.dto.create.AccountCreateDTO;
import com.spencer.payments.dto.response.AccountResponseDTO;
import com.spencer.payments.entity.Account;
import com.spencer.payments.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponseDTO> createAccount(@Valid @RequestBody AccountCreateDTO dto) {
        return ResponseEntity.ok(accountService.createAccount(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getAccount(@PathVariable UUID id) {
        return ResponseEntity.ok(accountService.getAccount(id));
    }
}
