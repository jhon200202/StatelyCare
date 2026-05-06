package com.statelycare.erp.nutrition.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record AssignDietRequest(
    @NotNull(message = "Resident ID is required")
    UUID residentId,
    @NotBlank(message = "Diet type is required")
    String dietType,
    String observations
) {}
