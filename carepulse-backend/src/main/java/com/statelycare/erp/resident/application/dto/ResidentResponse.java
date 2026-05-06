package com.statelycare.erp.resident.application.dto;

import com.statelycare.erp.resident.domain.model.Gender;
import com.statelycare.erp.resident.domain.model.ResidentStatus;
import java.time.LocalDate;
import java.util.UUID;

public record ResidentResponse(
    UUID id,
    String residentCode,
    String firstName,
    String lastName,
    LocalDate dateOfBirth,
    Gender gender,
    UUID roomId,
    LocalDate admissionDate,
    ResidentStatus status
) {}
