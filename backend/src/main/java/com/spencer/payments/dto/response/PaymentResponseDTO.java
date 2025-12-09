package com.spencer.payments.dto.response;

import com.spencer.payments.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record PaymentResponseDTO(
        UUID id,
        UUID sourceAccountId,
        UUID destAccountId,
        BigDecimal amount,
        String currency,
        PaymentStatus status,
        OffsetDateTime createdAt
) {}
