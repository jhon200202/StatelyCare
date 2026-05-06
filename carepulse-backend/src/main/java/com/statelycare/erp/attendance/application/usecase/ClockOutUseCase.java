package com.statelycare.erp.attendance.application.usecase;

import com.statelycare.erp.attendance.application.dto.AttendanceResponse;
import com.statelycare.erp.attendance.domain.model.AttendanceRecord;
import com.statelycare.erp.attendance.domain.repository.AttendanceRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.UUID;

@Service
public class ClockOutUseCase {

    private final AttendanceRepository attendanceRepository;

    public ClockOutUseCase(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public AttendanceResponse execute(UUID employeeId) {
        AttendanceRecord activeRecord = attendanceRepository.findActiveByStaffId(employeeId)
            .orElseThrow(() -> new IllegalStateException("No active clock-in found for this employee"));

        AttendanceRecord closedRecord = activeRecord.checkOut();
        AttendanceRecord saved = attendanceRepository.save(closedRecord);

        return new AttendanceResponse(
            saved.id(), saved.employeeId(), saved.shiftDate(), saved.scheduledStart(), saved.scheduledEnd(),
            saved.actualClockIn(), saved.actualClockOut(), saved.status(), saved.lateMinutes(),
            saved.totalHours(), saved.notes()
        );
    }
}
