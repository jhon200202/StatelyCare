package com.statelycare.erp.attendance.application.dto;

import com.statelycare.erp.attendance.domain.model.AttendanceStatus;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record AttendanceResponse(
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
    String notes
) {}
