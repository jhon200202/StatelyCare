package com.statelycare.erp.attendance.domain.model;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record AttendanceRecord(
    UUID id,
    UUID employeeId,
    LocalDate shiftDate,
    LocalTime scheduledStart,
    LocalTime scheduledEnd,
    Instant actualClockIn,
    Instant actualClockOut,
    AttendanceStatus status,
    int lateMinutes,
    Double totalHours,
    String notes,
    UUID approvedBy,
    Instant createdAt,
    Instant updatedAt
) {
    public static AttendanceRecord checkIn(UUID employeeId, LocalTime scheduledStart, LocalTime scheduledEnd) {
        return new AttendanceRecord(
            UUID.randomUUID(),
            employeeId,
            LocalDate.now(),
            scheduledStart,
            scheduledEnd,
            Instant.now(),
            null,
            AttendanceStatus.PRESENT,
            0,
            null,
            null,
            null,
            Instant.now(),
            Instant.now()
        );
    }

    public AttendanceRecord checkOut() {
        Instant now = Instant.now();
        double hours = Duration.between(this.actualClockIn, now).toMinutes() / 60.0;
        
        return new AttendanceRecord(
            this.id,
            this.employeeId,
            this.shiftDate,
            this.scheduledStart,
            this.scheduledEnd,
            this.actualClockIn,
            now,
            this.status,
            this.lateMinutes,
            Math.round(hours * 100.0) / 100.0,
            this.notes,
            this.approvedBy,
            this.createdAt,
            now
        );
    }
}
