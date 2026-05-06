package com.statelycare.erp.attendance.domain.repository;

import com.statelycare.erp.attendance.domain.model.AttendanceRecord;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceRepository {
    List<AttendanceRecord> findByWorkDate(LocalDate date);
    List<AttendanceRecord> findByWorkDateAndEmployeeId(LocalDate date, UUID employeeId);
    Optional<AttendanceRecord> findActiveByStaffId(UUID staffId);
    Optional<AttendanceRecord> findById(UUID id);
    AttendanceRecord save(AttendanceRecord record);
}
