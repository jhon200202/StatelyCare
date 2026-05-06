package com.statelycare.erp.attendance.application.usecase;

import com.statelycare.erp.attendance.application.dto.AttendanceResponse;
import com.statelycare.erp.attendance.domain.repository.AttendanceRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GetDailyAttendanceUseCase {

    private final AttendanceRepository attendanceRepository;

    public GetDailyAttendanceUseCase(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public List<AttendanceResponse> execute(LocalDate date, UUID employeeId) {
        var records = employeeId != null
                ? attendanceRepository.findByWorkDateAndEmployeeId(date, employeeId)
                : attendanceRepository.findByWorkDate(date);

        return records.stream()
            .map(r -> new AttendanceResponse(
                r.id(), r.employeeId(), r.shiftDate(), r.scheduledStart(), r.scheduledEnd(),
                r.actualClockIn(), r.actualClockOut(), r.status(), r.lateMinutes(),
                r.totalHours(), r.notes()
            ))
            .collect(Collectors.toList());
    }
}