package com.statelycare.erp.nutrition.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateMenuItemRequest(
    @NotBlank(message = "Name is required")
    String name,
    String description,
    String allergens,
    @PositiveOrZero(message = "Calories must be zero or positive")
    int calories
) {}
