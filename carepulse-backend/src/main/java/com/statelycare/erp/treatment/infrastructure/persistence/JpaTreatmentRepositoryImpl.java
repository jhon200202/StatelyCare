package com.statelycare.erp.treatment.infrastructure.persistence;

import com.statelycare.erp.treatment.domain.model.Treatment;
import com.statelycare.erp.treatment.domain.model.TreatmentStatus;
import com.statelycare.erp.treatment.domain.repository.TreatmentRepository;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JpaTreatmentRepositoryImpl implements TreatmentRepository {

    private final SpringDataTreatmentRepository repository;

    public JpaTreatmentRepositoryImpl(SpringDataTreatmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Treatment> findActiveByResidentId(UUID residentId) {
        return repository.findByDeletedAtIsNull().stream()
                .filter(e -> e.getResidentId().equals(residentId))
                .filter(e -> e.getStatus() == TreatmentStatus.ACTIVE)
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Treatment> findByResidentIdAndStatus(UUID residentId, TreatmentStatus status) {
        return repository.findByDeletedAtIsNull().stream()
                .filter(e -> e.getResidentId().equals(residentId))
                .filter(e -> e.getStatus() == status)
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Treatment> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Treatment save(Treatment treatment) {
        return toDomain(repository.save(toEntity(treatment)));
    }

    @Override
    public void deleteById(UUID id) {
        repository.findById(id).ifPresent(entity -> {
            entity.setDeletedAt(Instant.now());
            repository.save(entity);
        });
    }

    private Treatment toDomain(TreatmentEntity entity) {
        return new Treatment(
            entity.getId(),
            entity.getResidentId(),
            entity.getPrescribedBy(),
            entity.getTreatmentName(),
            entity.getTreatmentType(),
            entity.getDescription(),
            entity.getFrequency(),
            entity.getScheduledTime(),
            entity.getStartDate(),
            entity.getEndDate(),
            entity.getStatus(),
            entity.getInstructions(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    private TreatmentEntity toEntity(Treatment domain) {
        TreatmentEntity entity = new TreatmentEntity();
        entity.setId(domain.id());
        entity.setResidentId(domain.residentId());
        entity.setPrescribedBy(domain.prescribedBy());
        entity.setTreatmentName(domain.treatmentName());
        entity.setTreatmentType(domain.treatmentType());
        entity.setDescription(domain.description());
        entity.setFrequency(domain.frequency());
        entity.setScheduledTime(domain.scheduledTime());
        entity.setStartDate(domain.startDate());
        entity.setEndDate(domain.endDate());
        entity.setStatus(domain.status());
        entity.setInstructions(domain.instructions());
        entity.setCreatedAt(domain.createdAt());
        entity.setUpdatedAt(domain.updatedAt());
        return entity;
    }
}
