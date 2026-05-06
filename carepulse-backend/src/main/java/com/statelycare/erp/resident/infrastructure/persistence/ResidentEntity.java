package com.statelycare.erp.resident.infrastructure.persistence;

import com.statelycare.erp.resident.domain.model.Gender;
import com.statelycare.erp.resident.domain.model.ResidentStatus;
import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "residents")
public class ResidentEntity {
    
    @Id
    private UUID id;
    
    @Column(name = "resident_code", nullable = false, unique = true)
    private String residentCode;
    
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;
    
    @Column(name = "room_id")
    private UUID roomId;
    
    @Column(name = "admission_date", nullable = false)
    private LocalDate admissionDate;
    
    @Column(name = "primary_physician_id")
    private UUID primaryPhysicianId;
    
    @Column(name = "care_plan")
    private String carePlan;
    
    @Column(name = "medical_history_summary")
    private String medicalHistorySummary;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResidentStatus status;
    
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted_at")
private Instant deletedAt;

    public ResidentEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getResidentCode() { return residentCode; }
    public void setResidentCode(String residentCode) { this.residentCode = residentCode; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }
    public UUID getRoomId() { return roomId; }
    public void setRoomId(UUID roomId) { this.roomId = roomId; }
    public LocalDate getAdmissionDate() { return admissionDate; }
    public void setAdmissionDate(LocalDate admissionDate) { this.admissionDate = admissionDate; }
    public UUID getPrimaryPhysicianId() { return primaryPhysicianId; }
    public void setPrimaryPhysicianId(UUID primaryPhysicianId) { this.primaryPhysicianId = primaryPhysicianId; }
    public String getCarePlan() { return carePlan; }
    public void setCarePlan(String carePlan) { this.carePlan = carePlan; }
    public String getMedicalHistorySummary() { return medicalHistorySummary; }
    public void setMedicalHistorySummary(String medicalHistorySummary) { this.medicalHistorySummary = medicalHistorySummary; }
    public ResidentStatus getStatus() { return status; }
    public void setStatus(ResidentStatus status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public Instant getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Instant deletedAt) { this.deletedAt = deletedAt; }
}
