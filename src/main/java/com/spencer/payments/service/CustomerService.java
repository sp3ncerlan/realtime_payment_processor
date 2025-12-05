package com.spencer.payments.service;

import com.spencer.payments.dto.create.CustomerCreateDTO;
import com.spencer.payments.dto.response.CustomerResponseDTO;
import com.spencer.payments.entity.Customer;
import com.spencer.payments.repository.CustomerRepository;
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
