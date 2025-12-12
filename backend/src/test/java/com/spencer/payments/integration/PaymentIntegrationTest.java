package com.spencer.payments.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spencer.payments.payment.dto.request.PaymentRequestDTO;
import com.spencer.payments.account.entity.Account;
import com.spencer.payments.customer.entity.Customer;
import com.spencer.payments.account.repository.AccountRepository;
import com.spencer.payments.customer.repository.CustomerRepository;
import com.spencer.payments.payment.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class PaymentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    private Account sourceAccount;
    private Account destinationAccount;

    @BeforeEach
    void setup() {
        paymentRepository.deleteAll();
        accountRepository.deleteAll();
        customerRepository.deleteAll();

        Customer testCustomer1 = customerRepository.save(new Customer(null, "Sender", "sender@test.com", null));
        Customer testCustomer2 = customerRepository.save(new Customer(null, "Receiver", "receiver@test.com", null));

        // save accounts for the test customers
        sourceAccount = accountRepository.save(new Account(
                null,
                testCustomer1,
                "1001",
                "SAVINGS",
                "USD",
                new BigDecimal("1000.00"),
                null));

        destinationAccount = accountRepository.save(new Account(
                null,
                testCustomer2,
                "2001",
                "CHECKING",
                "USD",
                new BigDecimal("500.00"),
                null));
    }

    @Test
    void testSuccessfulPayment() throws Exception {
        // create payment request
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
        paymentRequest.setSourceAccountId(sourceAccount.getId());
        paymentRequest.setDestinationAccountId(destinationAccount.getId());
        paymentRequest.setAmount(new BigDecimal("100.00"));
        paymentRequest.setCurrency("USD");

        // perform the request and verify the response
        mockMvc.perform(post("/api/payments")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(100.00))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }
}