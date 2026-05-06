package com.statelycare.erp.resident.infrastructure.persistence;

import com.statelycare.erp.resident.domain.model.Resident;
import com.statelycare.erp.resident.domain.repository.ResidentRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JpaResidentRepositoryImpl implements ResidentRepository {

    private final SpringDataResidentRepository repository;

    public JpaResidentRepositoryImpl(SpringDataResidentRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Resident> findAll() {
        return repository.findByDeletedAtIsNull().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Resident> findAll(String query, String sortBy, String direction) {
        Sort sort = Sort.by(direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        return repository.searchResidents(query, sort).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Resident> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Resident save(Resident resident) {
        return toDomain(repository.save(toEntity(resident)));
    }

    @Override
    public void delete(Resident resident) {
        repository.findById(resident.id()).ifPresent(entity -> {
            entity.setDeletedAt(Instant.now());
            repository.save(entity);
        });
    }

    @Override
    public long count() {
        return repository.findByDeletedAtIsNull().size();
    }

    private Resident toDomain(ResidentEntity entity) {
        return new Resident(
            entity.getId(),
            entity.getResidentCode(),
            entity.getFirstName(),
            entity.getLastName(),
            entity.getDateOfBirth(),
            entity.getGender(),
            entity.getRoomId(),
            entity.getAdmissionDate(),
            entity.getPrimaryPhysicianId(),
            entity.getCarePlan(),
            entity.getMedicalHistorySummary(),
            entity.getStatus(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    private ResidentEntity toEntity(Resident domain) {
        ResidentEntity entity = new ResidentEntity();
        entity.setId(domain.id());
        entity.setResidentCode(domain.residentCode());
        entity.setFirstName(domain.firstName());
        entity.setLastName(domain.lastName());
        entity.setDateOfBirth(domain.dateOfBirth());
        entity.setGender(domain.gender());
        entity.setRoomId(domain.roomId());
        entity.setAdmissionDate(domain.admissionDate());
        entity.setPrimaryPhysicianId(domain.primaryPhysicianId());
        entity.setCarePlan(domain.carePlan());
        entity.setMedicalHistorySummary(domain.medicalHistorySummary());
        entity.setStatus(domain.status());
        entity.setCreatedAt(domain.createdAt());
        entity.setUpdatedAt(domain.updatedAt());
        return entity;
    }
}
