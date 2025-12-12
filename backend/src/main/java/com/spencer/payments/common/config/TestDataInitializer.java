package com.spencer.payments.common.config;

import com.spencer.payments.account.entity.Account;
import com.spencer.payments.account.entity.AccountType;
import com.spencer.payments.customer.entity.Customer;
import com.spencer.payments.account.repository.AccountRepository;
import com.spencer.payments.customer.repository.CustomerRepository;
import com.spencer.payments.payment.entity.Payment;
import com.spencer.payments.payment.entity.PaymentStatus;
import com.spencer.payments.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class TestDataInitializer {

    @Bean
    @Profile("dev")
    public CommandLineRunner initData(
            CustomerRepository customerRepository,
            AccountRepository accountRepository,
            PaymentRepository paymentRepository) {
        return args -> {
            log.info("=".repeat(80));
            log.info("INITIALIZING TEST DATA");
            log.info("=".repeat(80));

            // Create customers
            log.info("\n--- Creating Customers ---");
            Customer customer1 = createCustomer("John Doe", "john.doe@example.com");
            customerRepository.save(customer1);
            log.info("✓ Created Customer: {} ({})", customer1.getName(), customer1.getEmail());

            Customer customer2 = createCustomer("Jane Smith", "jane.smith@example.com");
            customerRepository.save(customer2);
            log.info("✓ Created Customer: {} ({})", customer2.getName(), customer2.getEmail());

            Customer customer3 = createCustomer("Bob Johnson", "bob.johnson@example.com");
            customerRepository.save(customer3);
            log.info("✓ Created Customer: {} ({})", customer3.getName(), customer3.getEmail());

            Customer customer4 = createCustomer("Alice Williams", "alice.williams@example.com");
            customerRepository.save(customer4);
            log.info("✓ Created Customer: {} ({})", customer4.getName(), customer4.getEmail());

            // Create accounts
            log.info("\n--- Creating Accounts ---");
            Account account1 = createAccount(customer1, "ACC-001", new BigDecimal("15000.00"), "USD", AccountType.CHECKING);
            accountRepository.save(account1);
            log.info("✓ Created Account: {} - {} ({}) - Balance: ${}",
                    account1.getAccountNumber(), customer1.getName(), account1.getAccountType(), account1.getBalance());

            Account account2 = createAccount(customer1, "ACC-002", new BigDecimal("8000.00"), "USD", AccountType.SAVINGS);
            accountRepository.save(account2);
            log.info("✓ Created Account: {} - {} ({}) - Balance: ${}",
                    account2.getAccountNumber(), customer1.getName(), account2.getAccountType(), account2.getBalance());

            Account account3 = createAccount(customer2, "ACC-003", new BigDecimal("25000.00"), "USD", AccountType.CHECKING);
            accountRepository.save(account3);
            log.info("✓ Created Account: {} - {} ({}) - Balance: ${}",
                    account3.getAccountNumber(), customer2.getName(), account3.getAccountType(), account3.getBalance());

            Account account4 = createAccount(customer2, "ACC-004", new BigDecimal("12000.00"), "USD", AccountType.SAVINGS);
            accountRepository.save(account4);
            log.info("✓ Created Account: {} - {} ({}) - Balance: ${}",
                    account4.getAccountNumber(), customer2.getName(), account4.getAccountType(), account4.getBalance());

            Account account5 = createAccount(customer3, "ACC-005", new BigDecimal("18500.00"), "USD", AccountType.CHECKING);
            accountRepository.save(account5);
            log.info("✓ Created Account: {} - {} ({}) - Balance: ${}",
                    account5.getAccountNumber(), customer3.getName(), account5.getAccountType(), account5.getBalance());

            Account account6 = createAccount(customer4, "ACC-006", new BigDecimal("9500.00"), "USD", AccountType.CHECKING);
            accountRepository.save(account6);
            log.info("✓ Created Account: {} - {} ({}) - Balance: ${}",
                    account6.getAccountNumber(), customer4.getName(), account6.getAccountType(), account6.getBalance());

            // Create payments across different months
            log.info("\n--- Creating Payments ---");
            List<Payment> payments = new ArrayList<>();

            // October 2024 payments
            log.info("\n** October 2024 Payments **");
            payments.add(createPayment(account1, account3, new BigDecimal("500.00"), PaymentStatus.COMPLETED,
                    OffsetDateTime.now().minusMonths(2).withDayOfMonth(5)));
            payments.add(createPayment(account2, account4, new BigDecimal("300.00"), PaymentStatus.COMPLETED,
                    OffsetDateTime.now().minusMonths(2).withDayOfMonth(8)));
            payments.add(createPayment(account3, account5, new BigDecimal("1200.00"), PaymentStatus.COMPLETED,
                    OffsetDateTime.now().minusMonths(2).withDayOfMonth(12)));
            payments.add(createPayment(account5, account1, new BigDecimal("750.00"), PaymentStatus.COMPLETED,
                    OffsetDateTime.now().minusMonths(2).withDayOfMonth(15)));
            payments.add(createPayment(account4, account6, new BigDecimal("450.00"), PaymentStatus.COMPLETED,
                    OffsetDateTime.now().minusMonths(2).withDayOfMonth(18)));
            payments.add(createPayment(account6, account2, new BigDecimal("220.00"), PaymentStatus.COMPLETED,
                    OffsetDateTime.now().minusMonths(2).withDayOfMonth(22)));
            payments.add(createPayment(account1, account5, new BigDecimal("890.00"), PaymentStatus.FAILED,
                    OffsetDateTime.now().minusMonths(2).withDayOfMonth(25)));
            payments.add(createPayment(account3, account6, new BigDecimal("340.00"), PaymentStatus.COMPLETED,
                    OffsetDateTime.now().minusMonths(2).withDayOfMonth(28)));

            // November 2024 payments
            log.info("\n** November 2024 Payments **");
            payments.add(createPayment(account1, account3, new BigDecimal("650.00"), PaymentStatus.COMPLETED,
                    OffsetDateTime.now().minusMonths(1).withDayOfMonth(2)));
            payments.add(createPayment(account3, account1, new BigDecimal("1100.00"), PaymentStatus.COMPLETED,
                    OffsetDateTime.now().minusMonths(1).withDayOfMonth(5)));
            payments.add(createPayment(account2, account5, new BigDecimal("420.00"), PaymentStatus.COMPLETED,
                    OffsetDateTime.now().minusMonths(1).withDayOfMonth(7)));
            payments.add(createPayment(account5, account4, new BigDecimal("980.00"), PaymentStatus.COMPLETED,
                    OffsetDateTime.now().minusMonths(1).withDayOfMonth(10)));
            payments.add(createPayment(account4, account1, new BigDecimal("560.00"), PaymentStatus.COMPLETED,
                    OffsetDateTime.now().minusMonths(1).withDayOfMonth(13)));
            payments.add(createPayment(account6, account3, new BigDecimal("780.00"), PaymentStatus.PENDING,
                    OffsetDateTime.now().minusMonths(1).withDayOfMonth(16)));
            payments.add(createPayment(account1, account6, new BigDecimal("310.00"), PaymentStatus.COMPLETED,
                    OffsetDateTime.now().minusMonths(1).withDayOfMonth(19)));
            payments.add(createPayment(account3, account2, new BigDecimal("1450.00"), PaymentStatus.COMPLETED,
                    OffsetDateTime.now().minusMonths(1).withDayOfMonth(21)));
            payments.add(createPayment(account5, account6, new BigDecimal("620.00"), PaymentStatus.COMPLETED,
                    OffsetDateTime.now().minusMonths(1).withDayOfMonth(24)));
            payments.add(createPayment(account2, account3, new BigDecimal("270.00"), PaymentStatus.FAILED,
                    OffsetDateTime.now().minusMonths(1).withDayOfMonth(27)));

            // December 2024 payments (current month)
            log.info("\n** December 2024 Payments (Current Month) **");
            payments.add(createPayment(account1, account3, new BigDecimal("850.00"), PaymentStatus.COMPLETED,
                    OffsetDateTime.now().withDayOfMonth(3)));
            payments.add(createPayment(account3, account5, new BigDecimal("1320.00"), PaymentStatus.COMPLETED,
                    OffsetDateTime.now().withDayOfMonth(6)));
            payments.add(createPayment(account2, account4, new BigDecimal("490.00"), PaymentStatus.COMPLETED,
                    OffsetDateTime.now().withDayOfMonth(8)));
            payments.add(createPayment(account5, account1, new BigDecimal("1150.00"), PaymentStatus.COMPLETED,
                    OffsetDateTime.now().withDayOfMonth(11)));
            payments.add(createPayment(account4, account6, new BigDecimal("730.00"), PaymentStatus.COMPLETED,
                    OffsetDateTime.now().withDayOfMonth(14)));
            payments.add(createPayment(account6, account2, new BigDecimal("380.00"), PaymentStatus.COMPLETED,
                    OffsetDateTime.now().withDayOfMonth(17)));
            payments.add(createPayment(account1, account4, new BigDecimal("920.00"), PaymentStatus.PENDING,
                    OffsetDateTime.now().withDayOfMonth(19)));
            payments.add(createPayment(account3, account6, new BigDecimal("560.00"), PaymentStatus.COMPLETED,
                    OffsetDateTime.now().withDayOfMonth(21)));
            payments.add(createPayment(account5, account2, new BigDecimal("1240.00"), PaymentStatus.COMPLETED,
                    OffsetDateTime.now().withDayOfMonth(23)));
            payments.add(createPayment(account2, account1, new BigDecimal("410.00"), PaymentStatus.COMPLETED,
                    OffsetDateTime.now().withDayOfMonth(26)));
            payments.add(createPayment(account4, account3, new BigDecimal("680.00"), PaymentStatus.FAILED,
                    OffsetDateTime.now().withDayOfMonth(28)));
            payments.add(createPayment(account6, account5, new BigDecimal("1580.00"), PaymentStatus.COMPLETED,
                    OffsetDateTime.now().withDayOfMonth(30)));

            // Save all payments and log details
            int completedCount = 0;
            int pendingCount = 0;
            int failedCount = 0;
            BigDecimal totalVolume = BigDecimal.ZERO;

            for (Payment payment : payments) {
                paymentRepository.save(payment);
                log.info("✓ Payment: ${} from {} to {} - {} ({})",
                        payment.getAmount(),
                        payment.getSourceAccount().getAccountNumber(),
                        payment.getDestinationAccount().getAccountNumber(),
                        payment.getStatus(),
                        payment.getCreatedAt().toLocalDate());

                if (payment.getStatus() == PaymentStatus.COMPLETED) {
                    completedCount++;
                    totalVolume = totalVolume.add(payment.getAmount());
                } else if (payment.getStatus() == PaymentStatus.PENDING) {
                    pendingCount++;
                } else if (payment.getStatus() == PaymentStatus.FAILED) {
                    failedCount++;
                }
            }

            // Summary
            log.info("\n" + "=".repeat(80));
            log.info("TEST DATA INITIALIZATION COMPLETE");
            log.info("=".repeat(80));
            log.info("Customers Created: {}", customerRepository.count());
            log.info("Accounts Created: {}", accountRepository.count());
            log.info("Total Payments: {}", payments.size());
            log.info("  - Completed: {}", completedCount);
            log.info("  - Pending: {}", pendingCount);
            log.info("  - Failed: {}", failedCount);
            log.info("Total Transaction Volume: ${}", totalVolume);
            log.info("=".repeat(80));
        };
    }

    private Customer createCustomer(String name, String email) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        return customer;
    }

    private Account createAccount(Customer customer, String accountNumberPrefix, BigDecimal balance,
                                  String currency, AccountType accountType) {
        Account account = new Account();
        account.setCustomer(customer);
        account.setAccountNumber(generateAccountNumber(accountNumberPrefix));
        account.setBalance(generateRandomAmount()); // Use random decimal balances
        account.setCurrency(currency);
        account.setAccountType(accountType);
        return account;
    }

    private Payment createPayment(Account source, Account destination, BigDecimal amount,
                                  PaymentStatus status, OffsetDateTime createdAt) {
        Payment payment = new Payment();
        payment.setSourceAccount(source);
        payment.setDestinationAccount(destination);
        payment.setAmount(generateRandomAmount()); // Use random decimal amounts
        payment.setCurrency("USD");
        payment.setStatus(status);
        payment.setCreatedAt(createdAt);
        return payment;
    }

    private String generateAccountNumber(String prefix) {
        Random random = new Random();
        int part1 = random.nextInt(9000) + 1000; // Generate 4-digit number
        int part2 = random.nextInt(9000) + 1000; // Generate another 4-digit number
        return String.format("%s-%04d-%04d", prefix, part1, part2);
    }

    private BigDecimal generateRandomAmount() {
        Random random = new Random();
        int wholePart = random.nextInt(900) + 100; // Generate 3-digit whole part
        int decimalPart = random.nextInt(100); // Generate 2-digit decimal part
        return new BigDecimal(String.format("%d.%02d", wholePart, decimalPart));
    }
}

