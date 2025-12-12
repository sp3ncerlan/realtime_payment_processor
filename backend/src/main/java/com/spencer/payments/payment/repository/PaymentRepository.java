package com.spencer.payments.payment.repository;

import com.spencer.payments.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    @Query("SELECT p FROM Payment p WHERE p.sourceAccount.id = :accountId OR p.destinationAccount.id = :accountId ORDER BY p.createdAt DESC")
    List<Payment> findByAccountId(@Param("accountId") UUID accountId);

    @Query("SELECT COUNT(p) from Payment p WHERE p.sourceAccount.id = :accountId OR destinationAccount.id = :accountId")
    Integer countByAccountId(@Param("accountId") UUID accountId);

    @Query("SELECT COALESCE(SUM(CASE " +
            "WHEN p.destinationAccount.id = :accountId THEN p.amount " +
            "WHEN p.sourceAccount.id = :accountId THEN -p.amount " +
            "ELSE 0 END), 0) " +
            "FROM Payment p " +
            "WHERE (p.sourceAccount.id = :accountId OR p.destinationAccount.id = :accountId) " +
            "AND p.createdAt >= :startDate " +
            "AND p.status = 'COMPLETED'")
    BigDecimal calculateMonthlyChange(@Param("accountId") UUID accountId,
                                      @Param("startDate") OffsetDateTime startDate);

}
