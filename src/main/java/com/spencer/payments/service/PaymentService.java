package com.spencer.payments.service;

import com.spencer.payments.entity.Account;
import com.spencer.payments.entity.Payment;
import com.spencer.payments.entity.PaymentStatus;
import com.spencer.payments.repository.AccountRepository;
import com.spencer.payments.repository.PaymentRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final AccountRepository accountRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public Payment processPayment(UUID sourceAccountId, UUID destinationAccountId, BigDecimal amount, String currency) {
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
        paymentRepository.save(payment);

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
            return paymentRepository.save(payment);
        } catch (Exception e) {
            // if any errors, mark payment as FAILED
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);

            // rethrow exception to ensure transaction rollback
            throw e;
        }
    }
}
