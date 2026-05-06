package com.statelycare.erp.nutrition.domain.model;

import java.time.LocalDate;
import java.util.UUID;

public record DailyMenu(
    UUID id,
    LocalDate menuDate,
    UUID menuItemId,
    MealType mealType,
    int servingsPlanned,
    Integer servingsActual,
    String notes,
    UUID residentId
) {
    public static DailyMenu create(LocalDate date, UUID menuItemId, MealType type, int servingsPlanned, UUID residentId) {
        return new DailyMenu(UUID.randomUUID(), date, menuItemId, type, servingsPlanned, null, null, residentId);
    }
}
