package com.bankcore.bankcore_backend.customer.dto.response;

import com.bankcore.bankcore_backend.customer.enums.CustomerStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerResponse(
    UUID id,
    String customerNumber,
    String firstName,
    String lastName,
    String email,
    String phone,
    LocalDate birthDate,
    CustomerStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}