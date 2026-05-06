package com.statelycare.erp.nutrition.application.dto;

import com.statelycare.erp.nutrition.domain.model.MealType;
import com.statelycare.erp.nutrition.domain.model.TextureModification;

public record MenuItemRequest(
    String name,
    String description,
    MealType mealType,
    TextureModification textureModification
) {}
