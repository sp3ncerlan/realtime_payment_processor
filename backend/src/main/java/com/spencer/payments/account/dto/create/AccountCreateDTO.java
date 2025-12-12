package com.spencer.payments.dto.create;

import com.spencer.payments.entity.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountCreateDTO(
        @NotNull(message = "Customer ID is required") UUID customerId,

        @NotBlank(message = "Currency is required") String currency,

        @NotBlank(message = "Account type is required (e.g., CHECKING, SAVINGS)") AccountType accountType,

        @PositiveOrZero(message = "Initial balance cannot be negative") BigDecimal initialBalance
) {
    public AccountCreateDTO {
        if (initialBalance == null) {
            initialBalance = BigDecimal.ZERO;
        }
    }
}
