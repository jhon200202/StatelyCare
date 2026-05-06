package com.statelycare.erp.attendance.application.usecase;

import com.statelycare.erp.attendance.application.dto.AttendanceResponse;
import com.statelycare.erp.attendance.domain.model.AttendanceRecord;
import com.statelycare.erp.attendance.domain.repository.AttendanceRepository;
import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.util.UUID;

@Service
public class ClockInUseCase {

    private final AttendanceRepository repository;

    public ClockInUseCase(AttendanceRepository repository) {
        this.repository = repository;
    }

    public AttendanceResponse execute(UUID employeeId) {
        // Default shift for simplicity in seeder/demo
        LocalTime scheduledStart = LocalTime.of(8, 0);
        LocalTime scheduledEnd = LocalTime.of(16, 0);
        
        AttendanceRecord record = AttendanceRecord.checkIn(employeeId, scheduledStart, scheduledEnd);
        AttendanceRecord saved = repository.save(record);
        return mapToResponse(saved);
    }

    private AttendanceResponse mapToResponse(AttendanceRecord r) {
        return new AttendanceResponse(
            r.id(), r.employeeId(), r.shiftDate(), r.scheduledStart(), r.scheduledEnd(),
            r.actualClockIn(), r.actualClockOut(), r.status(), r.lateMinutes(),
            r.totalHours(), r.notes()
        );
    }
}
