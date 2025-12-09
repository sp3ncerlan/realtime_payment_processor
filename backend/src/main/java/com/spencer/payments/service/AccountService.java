package com.spencer.payments.service;

import com.spencer.payments.dto.create.AccountCreateDTO;
import com.spencer.payments.dto.response.AccountResponseDTO;
import com.spencer.payments.entity.Account;
import com.spencer.payments.entity.Customer;
import com.spencer.payments.repository.AccountRepository;
import com.spencer.payments.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

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

    private AccountResponseDTO mapToDTO(Account account) {
        return new AccountResponseDTO(
                account.getId(),
                account.getCustomer().getId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getCurrency(),
                account.getBalance(),
                account.getCreatedAt()
        );
    }

    public AccountResponseDTO getAccount(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return mapToDTO(account);
    }
}
