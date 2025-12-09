package com.spencer.payments.controller;

import com.spencer.payments.dto.create.CustomerCreateDTO;
import com.spencer.payments.dto.response.CustomerResponseDTO;
import com.spencer.payments.entity.Customer;
import com.spencer.payments.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@Valid @RequestBody CustomerCreateDTO dto) {
        return ResponseEntity.ok(customerService.createCustomer(dto));
    }
}
