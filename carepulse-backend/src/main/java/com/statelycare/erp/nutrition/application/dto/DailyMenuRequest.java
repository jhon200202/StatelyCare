package com.statelycare.erp.nutrition.application.dto;

import com.statelycare.erp.nutrition.domain.model.MealType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

public record DailyMenuRequest(
    @NotNull(message = "Date is required")
    String date,
    @NotNull(message = "MealType is required")
    MealType mealType,
    @NotNull(message = "Menu item ID is required")
    UUID menuItemId,
    @Positive(message = "Servings planned must be greater than 0")
    int servingsPlanned,
    String notes,
    UUID residentId
) {}
