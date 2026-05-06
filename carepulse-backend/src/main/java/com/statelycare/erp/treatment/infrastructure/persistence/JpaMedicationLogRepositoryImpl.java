package com.statelycare.erp.treatment.infrastructure.persistence;

import com.statelycare.erp.treatment.domain.model.MedicationLog;
import com.statelycare.erp.treatment.domain.repository.MedicationLogRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JpaMedicationLogRepositoryImpl implements MedicationLogRepository {

    private final SpringDataMedicationLogRepository repository;

    public JpaMedicationLogRepositoryImpl(SpringDataMedicationLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<MedicationLog> findByResidentId(UUID residentId) {
        return repository.findByResidentIdOrderByAdministeredAtDesc(residentId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicationLog> findByTreatmentId(UUID treatmentId) {
        return repository.findByTreatmentIdOrderByAdministeredAtDesc(treatmentId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public MedicationLog save(MedicationLog log) {
        return toDomain(repository.save(toEntity(log)));
    }

    private MedicationLog toDomain(MedicationLogEntity entity) {
        return new MedicationLog(
            entity.getId(), entity.getTreatmentId(), entity.getResidentId(),
            entity.getStaffId(), entity.getAdministeredAt(), entity.getNotes()
        );
    }

    private MedicationLogEntity toEntity(MedicationLog domain) {
        MedicationLogEntity entity = new MedicationLogEntity();
        entity.setId(domain.id());
        entity.setTreatmentId(domain.treatmentId());
        entity.setResidentId(domain.residentId());
        entity.setStaffId(domain.staffId());
        entity.setAdministeredAt(domain.administeredAt());
        entity.setNotes(domain.notes());
        return entity;
    }
}
