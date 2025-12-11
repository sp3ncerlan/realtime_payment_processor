package com.spencer.payments.config;

import com.spencer.payments.entity.*;
import com.spencer.payments.repository.AccountRepository;
import com.spencer.payments.repository.CustomerRepository;
import com.spencer.payments.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class TestDataInitializer {

    @Bean
    @Profile("dev") // Only runs in 'dev' profile
    public CommandLineRunner initData(
            CustomerRepository customerRepository,
            AccountRepository accountRepository,
            PaymentRepository paymentRepository) {
        return args -> {
            log.info("Initializing test data...");

            // Create customers
            Customer customer1 = new Customer();
            customer1.setName("John Doe");
            customer1.setEmail("john.doe@example.com");
            customerRepository.save(customer1);

            Customer customer2 = new Customer();
            customer2.setName("Jane Smith");
            customer2.setEmail("jane.smith@example.com");
            customerRepository.save(customer2);

            Customer customer3 = new Customer();
            customer3.setName("Bob Johnson");
            customer3.setEmail("bob.johnson@example.com");
            customerRepository.save(customer3);

            // Create accounts
            Account account1 = new Account();
            account1.setCustomer(customer1);
            account1.setAccountNumber("ACC-001");
            account1.setBalance(new BigDecimal("5000.00"));
            account1.setCurrency("USD");
            account1.setAccountType(AccountType.CHECKING); // Add this
            accountRepository.save(account1);

            Account account2 = new Account();
            account2.setCustomer(customer1);
            account2.setAccountNumber("ACC-002");
            account2.setBalance(new BigDecimal("3000.00"));
            account2.setCurrency("USD");
            account2.setAccountType(AccountType.SAVINGS); // Add this
            accountRepository.save(account2);

            Account account3 = new Account();
            account3.setCustomer(customer2);
            account3.setAccountNumber("ACC-003");
            account3.setBalance(new BigDecimal("10000.00"));
            account3.setCurrency("USD");
            account3.setAccountType(AccountType.CHECKING); // Add this
            accountRepository.save(account3);

            Account account4 = new Account();
            account4.setCustomer(customer3);
            account4.setAccountNumber("ACC-004");
            account4.setBalance(new BigDecimal("7500.00"));
            account4.setCurrency("USD");
            account4.setAccountType(AccountType.CHECKING); // Add this
            accountRepository.save(account4);

            // Create dummy payments
            Payment payment1 = new Payment();
            payment1.setSourceAccount(account1);
            payment1.setDestinationAccount(account3);
            payment1.setAmount(new BigDecimal("500.00"));
            payment1.setCurrency("USD");
            payment1.setStatus(PaymentStatus.COMPLETED);
            paymentRepository.save(payment1);

            Payment payment2 = new Payment();
            payment2.setSourceAccount(account2);
            payment2.setDestinationAccount(account4);
            payment2.setAmount(new BigDecimal("250.00"));
            payment2.setCurrency("USD");
            payment2.setStatus(PaymentStatus.COMPLETED);
            paymentRepository.save(payment2);

            Payment payment3 = new Payment();
            payment3.setSourceAccount(account3);
            payment3.setDestinationAccount(account1);
            payment3.setAmount(new BigDecimal("1000.00"));
            payment3.setCurrency("USD");
            payment3.setStatus(PaymentStatus.PENDING);
            paymentRepository.save(payment3);

            Payment payment4 = new Payment();
            payment4.setSourceAccount(account4);
            payment4.setDestinationAccount(account2);
            payment4.setAmount(new BigDecimal("150.00"));
            payment4.setCurrency("USD");
            payment4.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment4);

            log.info("Test data initialized successfully");
        };
    }
}
