package com.spencer.payments.controller;

import com.spencer.payments.dto.create.CustomerCreateDTO;
import com.spencer.payments.dto.response.AccountResponseDTO;
import com.spencer.payments.dto.response.CustomerResponseDTO;
import com.spencer.payments.entity.Account;
import com.spencer.payments.entity.Customer;
import com.spencer.payments.repository.AccountRepository;
import com.spencer.payments.repository.CustomerRepository;
import com.spencer.payments.service.AccountService;
import com.spencer.payments.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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

    @GetMapping("/{customerId}/accounts")
    public ResponseEntity<List<AccountResponseDTO>> getCustomerAccounts(@PathVariable UUID customerId) {
        List<Account> accounts = accountRepository.findByCustomerId(customerId);

        List<AccountResponseDTO> accountDTOs = accounts.stream()
                .map(accountService::mapToDTO)
                .toList();

        return ResponseEntity.ok(accountDTOs);
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@Valid @RequestBody CustomerCreateDTO dto) {
        return ResponseEntity.ok(customerService.createCustomer(dto));
    }
}
