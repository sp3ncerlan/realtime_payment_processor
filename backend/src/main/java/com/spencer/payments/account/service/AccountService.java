package com.spencer.payments.account.service;

import com.spencer.payments.account.dto.create.AccountCreateDTO;
import com.spencer.payments.account.dto.response.AccountResponseDTO;
import com.spencer.payments.account.entity.Account;
import com.spencer.payments.customer.entity.Customer;
import com.spencer.payments.account.repository.AccountRepository;
import com.spencer.payments.customer.repository.CustomerRepository;
import com.spencer.payments.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;

    public AccountResponseDTO getAccount(UUID accountId) {
        return accountRepository.findById(accountId)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Transactional
    public AccountResponseDTO createAccount(AccountCreateDTO dto) {
        Customer customer = customerRepository.findById(dto.customerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Account account = new Account();
        account.setCustomer(customer);
        account.setCurrency(dto.currency());
        account.setAccountType(dto.accountType());
        account.setBalance(dto.initialBalance());

        account.setAccountNumber(String.valueOf(ThreadLocalRandom.current().nextLong(1000000000L, 9999999999L)));

        Account savedAccount = accountRepository.save(account);
        return mapToDTO(savedAccount);
    }

    public AccountResponseDTO mapToDTO(Account account) {
        BigDecimal previousBalance = previousMonthBalance(account.getId());
        BigDecimal balanceChange = account.getBalance().subtract(previousBalance);
        BigDecimal changePercentage = calculatePercentageChange(previousBalance, account.getBalance());

        return new AccountResponseDTO(
                account.getId(),
                account.getCustomer().getId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getCurrency(),
                account.getBalance(),
                account.getCreatedAt(),
                previousBalance,
                balanceChange,
                changePercentage
        );
    }

    private BigDecimal calculatePercentageChange(BigDecimal oldValue, BigDecimal newValue) {
        if (oldValue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return newValue.subtract(oldValue)
                .divide(oldValue, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
    }

    private BigDecimal previousMonthBalance(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        BigDecimal currentBalance = account.getBalance();

        OffsetDateTime startOfCurrentMonth = OffsetDateTime.now()
                .withDayOfMonth(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        BigDecimal currentMonthChange = paymentRepository.calculateMonthlyChange(accountId, startOfCurrentMonth);

        return currentBalance.subtract(currentMonthChange);
    }
}
