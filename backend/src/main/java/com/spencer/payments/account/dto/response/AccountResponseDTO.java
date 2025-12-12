package com.spencer.payments.account.dto.response;

import com.spencer.payments.account.entity.AccountType;

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
