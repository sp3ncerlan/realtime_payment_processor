package com.spencer.payments.payment.dto.response;

import com.spencer.payments.account.entity.AccountType;
import com.spencer.payments.payment.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record PaymentResponseDTO(
        UUID id,
        UUID sourceAccountId,
        UUID destAccountId,
        AccountType sourceAccountType,
        AccountType destAccountType,
        String sourceName,
        String destName,
        BigDecimal amount,
        String currency,
        PaymentStatus status,
        OffsetDateTime createdAt
) {}
