package com.statelycare.erp.treatment.domain.model;

import java.time.Instant;
import java.util.UUID;

public record MedicationAdministration(
    UUID id,
    UUID treatmentId,
    UUID administeredBy,
    Instant scheduledTime,
    Instant actualTime,
    String dosageGiven,
    MedicationRoute route,
    AdministrationStatus status,
    String notes,
    Integer painLevelBefore,
    Integer painLevelAfter,
    Instant createdAt,
    Instant updatedAt
) {
    public static MedicationAdministration schedule(UUID treatmentId, Instant scheduledTime, MedicationRoute route) {
        Instant now = Instant.now();
        return new MedicationAdministration(
            UUID.randomUUID(),
            treatmentId,
            null,
            scheduledTime,
            null,
            null,
            route,
            AdministrationStatus.PENDING,
            null,
            null,
            null,
            now,
            now
        );
    }
}
