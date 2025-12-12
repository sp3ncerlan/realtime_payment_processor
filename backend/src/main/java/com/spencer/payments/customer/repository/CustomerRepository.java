package com.spencer.payments.customer.repository;

import com.spencer.payments.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    // returns full customer object
    Optional<Customer> findByEmail(String email);
}