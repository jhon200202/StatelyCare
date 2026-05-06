package com.statelycare.erp.attendance.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.statelycare.erp.attendance.domain.model.AttendanceStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataAttendanceRepository extends JpaRepository<AttendanceEntity, UUID> {
    
    List<AttendanceEntity> findByShiftDate(LocalDate shiftDate);
    
    List<AttendanceEntity> findByShiftDateAndEmployeeId(LocalDate shiftDate, UUID employeeId);
    
    Optional<AttendanceEntity> findByEmployeeIdAndStatus(UUID employeeId, AttendanceStatus status);
}
