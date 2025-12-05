package com.spencer.payments.controller;

import com.spencer.payments.dto.request.PaymentRequestDTO;
import com.spencer.payments.dto.response.PaymentResponseDTO;
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
    public ResponseEntity<PaymentResponseDTO> createPayment(@Valid @RequestBody PaymentRequestDTO paymentRequest) {
        PaymentResponseDTO processedPayment = paymentService.processPayment(
                paymentRequest.sourceAccountId(),
                paymentRequest.destinationAccountId(),
                paymentRequest.amount(),
                paymentRequest.currency()
        );

        return ResponseEntity.ok(processedPayment);
    }
}
