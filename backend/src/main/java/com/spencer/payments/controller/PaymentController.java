package com.spencer.payments.controller;

import com.spencer.payments.dto.request.PaymentRequestDTO;
import com.spencer.payments.dto.response.PaymentResponseDTO;
import com.spencer.payments.entity.Payment;
import com.spencer.payments.service.PaymentService;
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

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<PaymentResponseDTO>> getCustomerPayments(@PathVariable UUID customerId) {
        List<PaymentResponseDTO> payments = paymentService.getPaymentsByCustomerId(customerId);

        return ResponseEntity.ok(payments);
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
