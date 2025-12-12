package com.spencer.payments.customer.service;

import com.spencer.payments.customer.dto.create.CustomerCreateDTO;
import com.spencer.payments.customer.dto.response.CustomerResponseDTO;
import com.spencer.payments.customer.entity.Customer;
import com.spencer.payments.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional
    public CustomerResponseDTO createCustomer(CustomerCreateDTO dto) {
        Customer customer = new Customer();
        customer.setName(dto.name());
        customer.setEmail(dto.email());

        Customer savedCustomer = customerRepository.save(customer);

        // Map Entity -> Response DTO
        return new CustomerResponseDTO(
                savedCustomer.getId(),
                savedCustomer.getName(),
                savedCustomer.getEmail(),
                savedCustomer.getCreatedAt()
        );
    }
}
