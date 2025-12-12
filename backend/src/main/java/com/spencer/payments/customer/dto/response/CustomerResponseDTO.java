package com.spencer.payments.customer.dto.response;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CustomerResponseDTO(
        UUID id,
        String name,
        String email,
        OffsetDateTime createdAt
) {}
