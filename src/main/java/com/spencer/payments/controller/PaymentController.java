package com.spencer.payments.controller;

import com.spencer.payments.dto.PaymentRequestDTO;
import com.spencer.payments.entity.Payment;
import com.spencer.payments.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody PaymentRequestDTO paymentRequest) {
        Payment processedPayment = paymentService.processPayment(
                paymentRequest.getSourceAccountId(),
                paymentRequest.getDestinationAccountId(),
                paymentRequest.getAmount(),
                paymentRequest.getCurrency()
        );

        return ResponseEntity.ok(processedPayment);
    }
}
