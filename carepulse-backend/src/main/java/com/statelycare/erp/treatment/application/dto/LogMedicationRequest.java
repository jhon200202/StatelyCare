package com.statelycare.erp.treatment.application.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record LogMedicationRequest(
    @NotNull(message = "Treatment ID is required")
    UUID treatmentId,
    
    @NotNull(message = "Resident ID is required")
    UUID residentId,
    
    @NotNull(message = "Staff ID is required")
    UUID staffId,
    
    String notes
) {}
