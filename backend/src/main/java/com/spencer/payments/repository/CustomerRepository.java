package com.spencer.payments.repository;

import com.spencer.payments.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    // returns full customer object
    Optional<Customer> findByUsername(String username);

    Optional<Customer> findByEmail(String email);

    // check for existence on username creation
    boolean existsByUsername(String username);
}