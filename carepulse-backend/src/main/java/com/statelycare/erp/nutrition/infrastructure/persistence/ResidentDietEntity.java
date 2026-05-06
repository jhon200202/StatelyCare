package com.statelycare.erp.nutrition.infrastructure.persistence;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "resident_diets")
public class ResidentDietEntity {
    
    @Id
    private UUID id;
    
    @Column(nullable = false, unique = true, name = "resident_id")
    private UUID residentId;
    
    @Column(nullable = false, name = "diet_type")
    private String dietType;
    
    @Column(length = 500)
    private String observations;
    
    @Column(nullable = false, name = "updated_at")
    private Instant updatedAt;

    public ResidentDietEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getResidentId() { return residentId; }
    public void setResidentId(UUID residentId) { this.residentId = residentId; }
    public String getDietType() { return dietType; }
    public void setDietType(String dietType) { this.dietType = dietType; }
    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
