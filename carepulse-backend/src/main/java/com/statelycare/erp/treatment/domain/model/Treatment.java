package com.statelycare.erp.treatment.domain.model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record Treatment(
    UUID id,
    UUID residentId,
    UUID prescribedBy,
    String treatmentName,
    TreatmentType treatmentType,
    String description,
    Frequency frequency,
    LocalTime scheduledTime,
    LocalDate startDate,
    LocalDate endDate,
    TreatmentStatus status,
    String instructions,
    Instant createdAt,
    Instant updatedAt
) {
    public static Treatment createNew(
            UUID residentId, 
            UUID prescribedBy, 
            String treatmentName, 
            TreatmentType treatmentType, 
            Frequency frequency, 
            LocalDate startDate) {
        Instant now = Instant.now();
        return new Treatment(
            UUID.randomUUID(),
            residentId,
            prescribedBy,
            treatmentName,
            treatmentType,
            null,
            frequency,
            null,
            startDate,
            null,
            TreatmentStatus.ACTIVE,
            null,
            now,
            now
        );
    }
}
