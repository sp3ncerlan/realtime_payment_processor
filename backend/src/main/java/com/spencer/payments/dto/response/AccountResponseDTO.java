package com.spencer.payments.dto.response;

import com.spencer.payments.entity.AccountType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record AccountResponseDTO(
        UUID id,
        UUID customerId,
        String accountNumber,
        AccountType accountType,
        String currency,
        BigDecimal balance,
        OffsetDateTime createdAt,
        BigDecimal previousMonthBalance,
        BigDecimal balanceChange,
        BigDecimal balanceChangePercentage
) {}
