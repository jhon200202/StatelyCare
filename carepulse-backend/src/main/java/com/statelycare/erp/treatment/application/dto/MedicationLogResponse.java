package com.statelycare.erp.treatment.application.dto;

import java.time.Instant;
import java.util.UUID;

public record MedicationLogResponse(
    UUID id,
    UUID treatmentId,
    UUID residentId,
    UUID staffId,
    Instant administeredAt,
    String notes
) {}
