package com.statelycare.erp.resident.application.dto;

import com.statelycare.erp.resident.domain.model.Gender;
import java.time.LocalDate;
import java.util.UUID;

public record ResidentRequest(
    String firstName,
    String lastName,
    LocalDate dateOfBirth,
    Gender gender,
    UUID roomId,
    LocalDate admissionDate
) {}
