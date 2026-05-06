package com.statelycare.erp.nutrition.infrastructure.persistence;

import com.statelycare.erp.nutrition.domain.model.MealType;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "daily_menus")
public class DailyMenuEntity {
    
    @Id
    private UUID id;
    
    @Column(name = "menu_date", nullable = false)
    private LocalDate menuDate;
    
    @Column(name = "menu_item_id", nullable = false)
    private UUID menuItemId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type", nullable = false)
    private MealType mealType;
    
    @Column(name = "servings_planned", nullable = false)
    private int servingsPlanned;
    
    @Column(name = "servings_actual")
    private Integer servingsActual;
    
    private String notes;
    
    @Column(name = "resident_id")
    private UUID residentId;

    public DailyMenuEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public LocalDate getMenuDate() { return menuDate; }
    public void setMenuDate(LocalDate menuDate) { this.menuDate = menuDate; }
    public UUID getMenuItemId() { return menuItemId; }
    public void setMenuItemId(UUID menuItemId) { this.menuItemId = menuItemId; }
    public MealType getMealType() { return mealType; }
    public void setMealType(MealType mealType) { this.mealType = mealType; }
    public int getServingsPlanned() { return servingsPlanned; }
    public void setServingsPlanned(int servingsPlanned) { this.servingsPlanned = servingsPlanned; }
    public Integer getServingsActual() { return servingsActual; }
    public void setServingsActual(Integer servingsActual) { this.servingsActual = servingsActual; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public UUID getResidentId() { return residentId; }
    public void setResidentId(UUID residentId) { this.residentId = residentId; }
}
