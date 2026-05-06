package com.statelycare.erp.nutrition.infrastructure.persistence;

import com.statelycare.erp.nutrition.domain.model.MealType;
import com.statelycare.erp.nutrition.domain.model.TextureModification;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "menu_items")
public class MenuItemEntity {
    
    @Id
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type", nullable = false)
    private MealType mealType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "texture_modification", nullable = false)
    private TextureModification textureModification;
    
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    public MenuItemEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public MealType getMealType() { return mealType; }
    public void setMealType(MealType mealType) { this.mealType = mealType; }
    public TextureModification getTextureModification() { return textureModification; }
    public void setTextureModification(TextureModification textureModification) { this.textureModification = textureModification; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
