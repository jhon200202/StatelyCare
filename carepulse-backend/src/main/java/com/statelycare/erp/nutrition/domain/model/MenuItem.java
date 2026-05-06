package com.statelycare.erp.nutrition.domain.model;

import java.util.UUID;

public record MenuItem(
    UUID id,
    String name,
    String description,
    MealType mealType,
    TextureModification textureModification,
    boolean isActive
) {
    public static MenuItem create(String name, String description, MealType mealType, TextureModification texture) {
        return new MenuItem(UUID.randomUUID(), name, description, mealType, texture, true);
    }
}
