package com.statelycare.erp.treatment.infrastructure.persistence;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "medication_logs")
public class MedicationLogEntity {
    
    @Id
    private UUID id;
    
    @Column(nullable = false, name = "treatment_id")
    private UUID treatmentId;
    
    @Column(nullable = false, name = "resident_id")
    private UUID residentId;
    
    @Column(nullable = false, name = "staff_id")
    private UUID staffId;
    
    @Column(nullable = false, name = "administered_at")
    private Instant administeredAt;
    
    @Column(name = "notes")
    private String notes;

    public MedicationLogEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTreatmentId() { return treatmentId; }
    public void setTreatmentId(UUID treatmentId) { this.treatmentId = treatmentId; }
    public UUID getResidentId() { return residentId; }
    public void setResidentId(UUID residentId) { this.residentId = residentId; }
    public UUID getStaffId() { return staffId; }
    public void setStaffId(UUID staffId) { this.staffId = staffId; }
    public Instant getAdministeredAt() { return administeredAt; }
    public void setAdministeredAt(Instant administeredAt) { this.administeredAt = administeredAt; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
