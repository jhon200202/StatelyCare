package com.statelycare.erp.treatment.domain.model;

import java.time.Instant;
import java.util.UUID;

public record MedicationLog(
    UUID id,
    UUID treatmentId,
    UUID residentId,
    UUID staffId,
    Instant administeredAt,
    String notes
) {
    public MedicationLog {
        if (treatmentId == null) throw new IllegalArgumentException("Treatment ID is required");
        if (residentId == null) throw new IllegalArgumentException("Resident ID is required");
        if (staffId == null) throw new IllegalArgumentException("Staff ID is required");
        if (administeredAt == null) throw new IllegalArgumentException("Administered At time is required");
    }

    public static MedicationLog record(UUID treatmentId, UUID residentId, UUID staffId, String notes) {
        return new MedicationLog(UUID.randomUUID(), treatmentId, residentId, staffId, Instant.now(), notes);
    }
}
