package com.statelycare.erp.attendance.infrastructure.web;

import com.statelycare.erp.attendance.application.dto.AttendanceResponse;
import com.statelycare.erp.attendance.application.usecase.ClockInUseCase;
import com.statelycare.erp.attendance.application.usecase.ClockOutUseCase;
import com.statelycare.erp.attendance.application.usecase.GetDailyAttendanceUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/attendance")
public class AttendanceController {

    private final ClockInUseCase clockInUseCase;
    private final ClockOutUseCase clockOutUseCase;
    private final GetDailyAttendanceUseCase getDailyAttendanceUseCase; 

    public AttendanceController(ClockInUseCase clockInUseCase,
                                ClockOutUseCase clockOutUseCase,
                                GetDailyAttendanceUseCase getDailyAttendanceUseCase) {
        this.clockInUseCase = clockInUseCase;
        this.clockOutUseCase = clockOutUseCase;
        this.getDailyAttendanceUseCase = getDailyAttendanceUseCase;
    }

    
    @PostMapping("/clock-in/{employeeId}")
    public ResponseEntity<AttendanceResponse> clockIn(@PathVariable UUID employeeId) {
        return ResponseEntity.ok(clockInUseCase.execute(employeeId));
    }

    @PostMapping("/clock-out/{employeeId}")
    public ResponseEntity<AttendanceResponse> clockOut(@PathVariable UUID employeeId) {
        return ResponseEntity.ok(clockOutUseCase.execute(employeeId));
    }

    
    @GetMapping("/daily")
    public ResponseEntity<List<AttendanceResponse>> getDailyAttendance(
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) UUID employeeId) {
        
        LocalDate targetDate = date != null ? date : LocalDate.now();
        return ResponseEntity.ok(getDailyAttendanceUseCase.execute(targetDate, employeeId));
    }
}