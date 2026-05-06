package com.statelycare.erp.attendance.infrastructure.persistence;

import com.statelycare.erp.attendance.domain.model.AttendanceStatus;
import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "attendance_records")
public class AttendanceEntity {
    
    @Id
    private UUID id;
    
    @Column(name = "employee_id", nullable = false)
    private UUID employeeId;
    
    @Column(name = "shift_date", nullable = false)
    private LocalDate shiftDate;
    
    @Column(name = "scheduled_start", nullable = false)
    private LocalTime scheduledStart;
    
    @Column(name = "scheduled_end", nullable = false)
    private LocalTime scheduledEnd;
    
    @Column(name = "actual_clock_in")
    private Instant actualClockIn;
    
    @Column(name = "actual_clock_out")
    private Instant actualClockOut;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;
    
    @Column(name = "late_minutes", nullable = false)
    private int lateMinutes;
    
    @Column(name = "total_hours")
    private Double totalHours;
    
    private String notes;
    
    @Column(name = "approved_by")
    private UUID approvedBy;
    
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public AttendanceEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getEmployeeId() { return employeeId; }
    public void setEmployeeId(UUID employeeId) { this.employeeId = employeeId; }
    public LocalDate getShiftDate() { return shiftDate; }
    public void setShiftDate(LocalDate shiftDate) { this.shiftDate = shiftDate; }
    public LocalTime getScheduledStart() { return scheduledStart; }
    public void setScheduledStart(LocalTime scheduledStart) { this.scheduledStart = scheduledStart; }
    public LocalTime getScheduledEnd() { return scheduledEnd; }
    public void setScheduledEnd(LocalTime scheduledEnd) { this.scheduledEnd = scheduledEnd; }
    public Instant getActualClockIn() { return actualClockIn; }
    public void setActualClockIn(Instant actualClockIn) { this.actualClockIn = actualClockIn; }
    public Instant getActualClockOut() { return actualClockOut; }
    public void setActualClockOut(Instant actualClockOut) { this.actualClockOut = actualClockOut; }
    public AttendanceStatus getStatus() { return status; }
    public void setStatus(AttendanceStatus status) { this.status = status; }
    public int getLateMinutes() { return lateMinutes; }
    public void setLateMinutes(int lateMinutes) { this.lateMinutes = lateMinutes; }
    public Double getTotalHours() { return totalHours; }
    public void setTotalHours(Double totalHours) { this.totalHours = totalHours; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public UUID getApprovedBy() { return approvedBy; }
    public void setApprovedBy(UUID approvedBy) { this.approvedBy = approvedBy; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
