package com.statelycare.erp.attendance.infrastructure.persistence;

import com.statelycare.erp.attendance.domain.model.AttendanceRecord;
import com.statelycare.erp.attendance.domain.repository.AttendanceRepository;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import com.statelycare.erp.attendance.domain.model.AttendanceStatus;

@Component
public class JpaAttendanceRepositoryImpl implements AttendanceRepository {

    private final SpringDataAttendanceRepository repository;

    public JpaAttendanceRepositoryImpl(SpringDataAttendanceRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<AttendanceRecord> findByWorkDate(LocalDate date) {
        return repository.findByShiftDate(date).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceRecord> findByWorkDateAndEmployeeId(LocalDate date, UUID employeeId) {
        return repository.findByShiftDateAndEmployeeId(date, employeeId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    @Override
    public Optional<AttendanceRecord> findActiveByStaffId(UUID staffId) {
        return repository.findAll().stream()
                .filter(e -> e.getEmployeeId().equals(staffId) && e.getActualClockOut() == null)
                .findFirst()
                .map(this::toDomain);
    }

    @Override
    public Optional<AttendanceRecord> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public AttendanceRecord save(AttendanceRecord record) {
        return toDomain(repository.save(toEntity(record)));
    }

    private AttendanceRecord toDomain(AttendanceEntity entity) {
        return new AttendanceRecord(
            entity.getId(),
            entity.getEmployeeId(),
            entity.getShiftDate(),
            entity.getScheduledStart(),
            entity.getScheduledEnd(),
            entity.getActualClockIn(),
            entity.getActualClockOut(),
            entity.getStatus(),
            entity.getLateMinutes(),
            entity.getTotalHours(),
            entity.getNotes(),
            entity.getApprovedBy(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    private AttendanceEntity toEntity(AttendanceRecord domain) {
        AttendanceEntity entity = new AttendanceEntity();
        entity.setId(domain.id());
        entity.setEmployeeId(domain.employeeId());
        entity.setShiftDate(domain.shiftDate());
        entity.setScheduledStart(domain.scheduledStart());
        entity.setScheduledEnd(domain.scheduledEnd());
        entity.setActualClockIn(domain.actualClockIn());
        entity.setActualClockOut(domain.actualClockOut());
        entity.setStatus(domain.status());
        entity.setLateMinutes(domain.lateMinutes());
        entity.setTotalHours(domain.totalHours());
        entity.setNotes(domain.notes());
        entity.setApprovedBy(domain.approvedBy());
        entity.setCreatedAt(domain.createdAt());
        entity.setUpdatedAt(domain.updatedAt());
        return entity;
    }
}
