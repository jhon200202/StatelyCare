package com.statelycare.erp.nutrition.application.dto;

import com.statelycare.erp.nutrition.domain.model.MealType;
import com.statelycare.erp.nutrition.domain.model.TextureModification;
import java.util.UUID;

public record MenuItemResponse(
    UUID id,
    String name,
    String description,
    MealType mealType,
    TextureModification textureModification,
    boolean isActive
) {}
