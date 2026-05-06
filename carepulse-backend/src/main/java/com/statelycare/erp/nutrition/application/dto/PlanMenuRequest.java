package com.statelycare.erp.nutrition.application.dto;

import com.statelycare.erp.nutrition.domain.model.MealType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record PlanMenuRequest(
    @NotNull(message = "Date is required")
    LocalDate date,
    @NotNull(message = "Meal type is required")
    MealType mealType,
    @NotEmpty(message = "At least one menu item is required")
    List<UUID> menuItemIds
) {}
