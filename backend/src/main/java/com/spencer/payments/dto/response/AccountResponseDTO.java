package com.spencer.payments.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record AccountResponseDTO(
        UUID id,
        UUID customerId,
        String accountNumber,
        String accountType,
        String currency,
        BigDecimal balance,
        OffsetDateTime createdAt
) {}
