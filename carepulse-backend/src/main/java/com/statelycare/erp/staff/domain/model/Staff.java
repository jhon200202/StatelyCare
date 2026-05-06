package com.statelycare.erp.staff.domain.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record Staff(
    UUID id,
    String firstName,
    String lastName,
    String professionalRole,
    String shift,
    LocalDate hireDate,
    boolean isActive,
    Instant createdAt,
    Instant updatedAt
) {
    public Staff {
        if (firstName == null || firstName.isBlank()) throw new IllegalArgumentException("First name is required");
        if (lastName == null || lastName.isBlank()) throw new IllegalArgumentException("Last name is required");
        if (professionalRole == null || professionalRole.isBlank()) throw new IllegalArgumentException("Role is required");
    }

    public static Staff createNew(String firstName, String lastName, String professionalRole, String shift, LocalDate hireDate) {
        Instant now = Instant.now();
        return new Staff(UUID.randomUUID(), firstName, lastName, professionalRole, shift, hireDate, true, now, now);
    }
}
