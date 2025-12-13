package com.spencer.payments.payment.service;

import com.spencer.payments.payment.controller.PaymentWebSocketController;
import com.spencer.payments.payment.dto.response.PaymentResponseDTO;
import com.spencer.payments.account.entity.Account;
import com.spencer.payments.payment.entity.Payment;
import com.spencer.payments.payment.entity.PaymentStatus;
import com.spencer.payments.account.repository.AccountRepository;
import com.spencer.payments.payment.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final AccountRepository accountRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentWebSocketController paymentWebSocketController;

    public Integer getTotalTransactions(UUID accountId) {
        return paymentRepository.countByAccountId(accountId);
    }

    public List<PaymentResponseDTO> getPaymentsByAccountId(UUID customerId) {
        List<Payment> payments =  paymentRepository.findByAccountId(customerId);

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

            log.info("Source account balance before: {}", sourceAccount.getBalance());
            log.info("Destination account balance before: {}", destinationAccount.getBalance());

            // perform the debit and credit operations
            sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
            destinationAccount.setBalance(destinationAccount.getBalance().add(amount));

            log.info("Source account balance after: {}", sourceAccount.getBalance());
            log.info("Destination account balance after: {}", destinationAccount.getBalance());

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

        log.info("Sending payment update for accountId: {} with payment status: {}", sourceAccountId, payment.getStatus());

        paymentWebSocketController.sendPaymentUpdate(sourceAccountId, new PaymentResponseDTO(
                payment.getId(),
                payment.getSourceAccount().getId(),
                payment.getDestinationAccount().getId(),
                payment.getSourceAccount().getAccountType(),
                payment.getDestinationAccount().getAccountType(),
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
                payment.getSourceAccount().getAccountType(),
                payment.getDestinationAccount().getAccountType(),
                payment.getSourceAccount().getCustomer().getName(),
                payment.getDestinationAccount().getCustomer().getName(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getStatus(),
                payment.getCreatedAt()
        );
    }
}
