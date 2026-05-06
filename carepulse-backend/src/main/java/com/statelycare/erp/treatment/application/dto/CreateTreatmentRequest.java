package com.statelycare.erp.treatment.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public record CreateTreatmentRequest(
    @NotNull(message = "Resident ID is required")
    UUID residentId,
    
    @NotBlank(message = "Medication name is required")
    String medicationName,
    
    @NotBlank(message = "Dosage is required")
    String dosage,
    
    String frequency,
    
    @NotNull(message = "Start date is required")
    LocalDate startDate,
    
    LocalDate endDate
) {}
