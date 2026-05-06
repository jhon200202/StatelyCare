package com.statelycare.erp.treatment.infrastructure.persistence;

import com.statelycare.erp.treatment.domain.model.MedicationAdministration;
import com.statelycare.erp.treatment.domain.repository.MedicationAdministrationRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JpaMedicationAdministrationRepositoryImpl implements MedicationAdministrationRepository {

    private final SpringDataMedicationAdministrationRepository repository;

    public JpaMedicationAdministrationRepositoryImpl(SpringDataMedicationAdministrationRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<MedicationAdministration> findAll() {
        return repository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<MedicationAdministration> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public MedicationAdministration save(MedicationAdministration administration) {
        return toDomain(repository.save(toEntity(administration)));
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    private MedicationAdministration toDomain(MedicationAdministrationEntity entity) {
        return new MedicationAdministration(
            entity.getId(),
            entity.getTreatmentId(),
            entity.getAdministeredBy(),
            entity.getScheduledTime(),
            entity.getActualTime(),
            entity.getDosageGiven(),
            entity.getRoute(),
            entity.getStatus(),
            entity.getNotes(),
            entity.getPainLevelBefore(),
            entity.getPainLevelAfter(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    private MedicationAdministrationEntity toEntity(MedicationAdministration domain) {
        MedicationAdministrationEntity entity = new MedicationAdministrationEntity();
        entity.setId(domain.id());
        entity.setTreatmentId(domain.treatmentId());
        entity.setAdministeredBy(domain.administeredBy());
        entity.setScheduledTime(domain.scheduledTime());
        entity.setActualTime(domain.actualTime());
        entity.setDosageGiven(domain.dosageGiven());
        entity.setRoute(domain.route());
        entity.setStatus(domain.status());
        entity.setNotes(domain.notes());
        entity.setPainLevelBefore(domain.painLevelBefore());
        entity.setPainLevelAfter(domain.painLevelAfter());
        entity.setCreatedAt(domain.createdAt());
        entity.setUpdatedAt(domain.updatedAt());
        return entity;
    }
}
