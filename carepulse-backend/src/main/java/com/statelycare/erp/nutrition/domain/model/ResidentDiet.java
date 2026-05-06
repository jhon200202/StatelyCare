package com.statelycare.erp.nutrition.domain.model;

import java.time.Instant;
import java.util.UUID;

public record ResidentDiet(
    UUID id,
    UUID residentId,
    String dietType, // e.g., "Regular", "Low Sodium", "Diabetic"
    String observations,
    Instant updatedAt
) {
    public ResidentDiet {
        if (residentId == null) throw new IllegalArgumentException("Resident ID is required");
        if (dietType == null || dietType.isBlank()) throw new IllegalArgumentException("Diet type is required");
    }

    public static ResidentDiet assign(UUID residentId, String dietType, String observations) {
        return new ResidentDiet(UUID.randomUUID(), residentId, dietType, observations, Instant.now());
    }
}
