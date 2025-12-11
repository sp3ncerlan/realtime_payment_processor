package com.spencer.payments.repository;

import com.spencer.payments.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    @Query("SELECT p FROM Payment p WHERE p.sourceAccount.customer.id = :customerId OR p.destinationAccount.customer.id = :customerId ORDER BY p.createdAt DESC")
    List<Payment> findByCustomerId(@Param("customerId") UUID customerId);
}
