package com.statelycare.erp.resident.domain.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record Resident(
    UUID id,
    String residentCode,
    String firstName,
    String lastName,
    LocalDate dateOfBirth,
    Gender gender,
    UUID roomId,
    LocalDate admissionDate,
    UUID primaryPhysicianId,
    String carePlan,
    String medicalHistorySummary,
    ResidentStatus status,
    Instant createdAt,
    Instant updatedAt
) {
    public static Resident createNew(
            String residentCode, 
            String firstName, 
            String lastName, 
            LocalDate dateOfBirth, 
            Gender gender, 
            UUID roomId, 
            LocalDate admissionDate) {
        Instant now = Instant.now();
        return new Resident(
            UUID.randomUUID(),
            residentCode,
            firstName,
            lastName,
            dateOfBirth,
            gender,
            roomId,
            admissionDate,
            null,
            null,
            null,
            ResidentStatus.ON_SITE,
            now,
            now
        );
    }

    public Resident update(
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            Gender gender,
            UUID roomId,
            LocalDate admissionDate) {
        return new Resident(
            this.id,
            this.residentCode,
            firstName,
            lastName,
            dateOfBirth,
            gender,
            roomId,
            admissionDate,
            this.primaryPhysicianId,
            this.carePlan,
            this.medicalHistorySummary,
            this.status,
            this.createdAt,
            Instant.now()
        );
    }
}