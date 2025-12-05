package com.spencer.payments.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PaymentRequestDTO {

    @NotNull(message = "Source account ID is required")
    private UUID sourceAccountId;

    @NotNull(message = "Destination account ID is required")
    private UUID destinationAccountId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than $0")
    @Digits(integer = 15, fraction = 4, message = "Amount format is invalid")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be 3 characters")
    private String currency;
}