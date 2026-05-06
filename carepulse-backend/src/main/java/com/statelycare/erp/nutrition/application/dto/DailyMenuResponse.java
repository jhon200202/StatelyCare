package com.statelycare.erp.nutrition.application.dto;

import com.statelycare.erp.nutrition.domain.model.MealType;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record DailyMenuResponse(
    UUID id,
    LocalDate date,
    MealType mealType,
    List<MenuItemResponse> items
) {}
