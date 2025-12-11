package com.spencer.payments.service;

import com.spencer.payments.controller.PaymentWebSocketController;
import com.spencer.payments.dto.response.PaymentResponseDTO;
import com.spencer.payments.entity.Account;
import com.spencer.payments.entity.Payment;
import com.spencer.payments.entity.PaymentStatus;
import com.spencer.payments.repository.AccountRepository;
import com.spencer.payments.repository.PaymentRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final AccountRepository accountRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentWebSocketController paymentWebSocketController;

    public List<PaymentResponseDTO> getPaymentsByCustomerId(UUID customerId) {
        List<Payment> payments =  paymentRepository.findByCustomerId(customerId);

        return payments.stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Transactional
    public PaymentResponseDTO processPayment(UUID customerId, UUID sourceAccountId, UUID destinationAccountId, BigDecimal amount, String currency) {
        // Find accounts involved and lock when used
        Account sourceAccount = accountRepository.findWithLockingById(sourceAccountId)
                .orElseThrow(() -> new RuntimeException("Source account not found with id: " + sourceAccountId));
        Account destinationAccount = accountRepository.findWithLockingById(destinationAccountId)
                .orElseThrow(() -> new RuntimeException("Destination account not found with id " + destinationAccountId));

        // Create and save initial payment record with PENDING state
        Payment payment = new Payment();
        payment.setSourceAccount(sourceAccount);
        payment.setDestinationAccount(destinationAccount);
        payment.setAmount(amount);
        payment.setCurrency(currency);
        payment.setStatus(PaymentStatus.PENDING);
        Payment savedPayment = paymentRepository.save(payment);

        try {
            // check for sufficient funds
            if (sourceAccount.getBalance().compareTo(amount) < 0) {
                throw new IllegalStateException("Insufficient funds in source account " + sourceAccountId);
            }

            // perform the debit and credit operations
            sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
            destinationAccount.setBalance(destinationAccount.getBalance().add(amount));

            // mark payment as completed
            payment.setStatus(PaymentStatus.COMPLETED);
            savedPayment = paymentRepository.save(payment);
        } catch (Exception e) {
            // if any errors, mark payment as FAILED
            savedPayment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(savedPayment);

            // rethrow exception to ensure transaction rollback
            throw e;
        }

        paymentWebSocketController.sendPaymentUpdate(new PaymentResponseDTO(
                payment.getId(),
                payment.getSourceAccount().getId(),
                payment.getDestinationAccount().getId(),
                payment.getSourceAccount().getCustomer().getName(),
                payment.getDestinationAccount().getCustomer().getName(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getStatus(),
                payment.getCreatedAt()
        ));

        return mapToDTO(savedPayment);
    }

    private PaymentResponseDTO mapToDTO(Payment payment) {
        return new PaymentResponseDTO(
                payment.getId(),
                payment.getSourceAccount().getId(),
                payment.getDestinationAccount().getId(),
                payment.getSourceAccount().getCustomer().getName(),
                payment.getDestinationAccount().getCustomer().getName(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getStatus(),
                payment.getCreatedAt()
        );
    }
}
