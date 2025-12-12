package com.spencer.payments.customer.controller;

import com.spencer.payments.customer.dto.create.CustomerCreateDTO;
import com.spencer.payments.account.dto.response.AccountResponseDTO;
import com.spencer.payments.customer.dto.response.CustomerResponseDTO;
import com.spencer.payments.account.entity.Account;
import com.spencer.payments.customer.entity.Customer;
import com.spencer.payments.account.repository.AccountRepository;
import com.spencer.payments.customer.repository.CustomerRepository;
import com.spencer.payments.account.service.AccountService;
import com.spencer.payments.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final AccountService accountService;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    // returns a list of all customers that exist
    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();

        List<CustomerResponseDTO> customerDTOs = customers.stream()
                .map(customer -> new CustomerResponseDTO(
                        customer.getId(),
                        customer.getName(),
                        customer.getEmail(),
                        customer.getCreatedAt()
                ))
                .toList();

        return ResponseEntity.ok(customerDTOs);
    }

    // returns a single customer based on their customerId
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDTO> getCustomer(@PathVariable UUID customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException(("Customer not found")));

        return ResponseEntity.ok(new CustomerResponseDTO(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getCreatedAt()
        ));
    }

    // returns all accounts for a single customer based on their customerId
    @GetMapping("/{customerId}/accounts")
    public ResponseEntity<List<AccountResponseDTO>> getCustomerAccounts(@PathVariable UUID customerId) {
        List<Account> accounts = accountRepository.findByCustomerId(customerId);

        List<AccountResponseDTO> accountDTOs = accounts.stream()
                .map(accountService::mapToDTO)
                .toList();

        return ResponseEntity.ok(accountDTOs);
    }

    // creates a new customer
    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@Valid @RequestBody CustomerCreateDTO dto) {
        return ResponseEntity.ok(customerService.createCustomer(dto));
    }
}
