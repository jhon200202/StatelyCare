package com.statelycare.erp.nutrition.application.dto;

public record DailyMenuItemResponse(
    String id,
    String date,
    String mealType,
    String menuItemId,
    int servingsPlanned,
    String notes,
    String residentId
) {}
