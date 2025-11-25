package com.spencer.payments.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PaymentRequestDTO {
    private UUID sourceAccountId;
    private UUID destinationAccountId;
    private BigDecimal amount;
    private String currency;
}