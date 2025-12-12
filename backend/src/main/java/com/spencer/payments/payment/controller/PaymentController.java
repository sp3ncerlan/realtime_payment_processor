package com.spencer.payments.payment.controller;

import com.spencer.payments.payment.dto.request.PaymentRequestDTO;
import com.spencer.payments.payment.dto.response.PaymentResponseDTO;
import com.spencer.payments.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/customer/{accountId}")
    public ResponseEntity<List<PaymentResponseDTO>> getCustomerPayments(@PathVariable UUID accountId) {
        List<PaymentResponseDTO> payments = paymentService.getPaymentsByAccountId(accountId);

        return ResponseEntity.ok(payments);
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<Integer> getTotalTransactions(@PathVariable UUID accountId) {
        return ResponseEntity.ok(paymentService.getTotalTransactions(accountId));
    }

    @PostMapping("/customer/{customerId}")
    public ResponseEntity<PaymentResponseDTO> createPayment(@PathVariable UUID customerId, @Valid @RequestBody PaymentRequestDTO paymentRequest) {
        PaymentResponseDTO processedPayment = paymentService.processPayment(
                customerId,
                paymentRequest.sourceAccountId(),
                paymentRequest.destinationAccountId(),
                paymentRequest.amount(),
                paymentRequest.currency()
        );

        return ResponseEntity.ok(processedPayment);
    }
}
