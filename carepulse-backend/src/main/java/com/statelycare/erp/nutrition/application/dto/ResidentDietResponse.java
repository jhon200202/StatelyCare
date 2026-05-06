package com.statelycare.erp.nutrition.application.dto;

import java.time.Instant;
import java.util.UUID;

public record ResidentDietResponse(
    UUID id,
    UUID residentId,
    String dietType,
    String observations,
    Instant updatedAt
) {}
