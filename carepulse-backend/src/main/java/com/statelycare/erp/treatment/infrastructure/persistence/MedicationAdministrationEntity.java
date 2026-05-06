package com.statelycare.erp.treatment.infrastructure.persistence;

import com.statelycare.erp.treatment.domain.model.AdministrationStatus;
import com.statelycare.erp.treatment.domain.model.MedicationRoute;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "medication_administrations")
public class MedicationAdministrationEntity {
    
    @Id
    private UUID id;
    
    @Column(name = "treatment_id", nullable = false)
    private UUID treatmentId;
    
    @Column(name = "administered_by")
    private UUID administeredBy;
    
    @Column(name = "scheduled_time", nullable = false)
    private Instant scheduledTime;
    
    @Column(name = "actual_time")
    private Instant actualTime;
    
    @Column(name = "dosage_given")
    private String dosageGiven;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MedicationRoute route;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdministrationStatus status;
    
    private String notes;
    
    @Column(name = "pain_level_before")
    private Integer painLevelBefore;
    
    @Column(name = "pain_level_after")
    private Integer painLevelAfter;
    
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public MedicationAdministrationEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTreatmentId() { return treatmentId; }
    public void setTreatmentId(UUID treatmentId) { this.treatmentId = treatmentId; }
    public UUID getAdministeredBy() { return administeredBy; }
    public void setAdministeredBy(UUID administeredBy) { this.administeredBy = administeredBy; }
    public Instant getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(Instant scheduledTime) { this.scheduledTime = scheduledTime; }
    public Instant getActualTime() { return actualTime; }
    public void setActualTime(Instant actualTime) { this.actualTime = actualTime; }
    public String getDosageGiven() { return dosageGiven; }
    public void setDosageGiven(String dosageGiven) { this.dosageGiven = dosageGiven; }
    public MedicationRoute getRoute() { return route; }
    public void setRoute(MedicationRoute route) { this.route = route; }
    public AdministrationStatus getStatus() { return status; }
    public void setStatus(AdministrationStatus status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Integer getPainLevelBefore() { return painLevelBefore; }
    public void setPainLevelBefore(Integer painLevelBefore) { this.painLevelBefore = painLevelBefore; }
    public Integer getPainLevelAfter() { return painLevelAfter; }
    public void setPainLevelAfter(Integer painLevelAfter) { this.painLevelAfter = painLevelAfter; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
