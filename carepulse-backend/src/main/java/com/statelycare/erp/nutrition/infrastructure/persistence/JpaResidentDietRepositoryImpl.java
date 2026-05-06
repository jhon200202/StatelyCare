package com.statelycare.erp.nutrition.infrastructure.persistence;

import com.statelycare.erp.nutrition.domain.model.ResidentDiet;
import com.statelycare.erp.nutrition.domain.repository.ResidentDietRepository;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;

@Component
public class JpaResidentDietRepositoryImpl implements ResidentDietRepository {

    private final SpringDataResidentDietRepository repository;

    public JpaResidentDietRepositoryImpl(SpringDataResidentDietRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<ResidentDiet> findByResidentId(UUID residentId) {
        return repository.findByResidentId(residentId).map(this::toDomain);
    }

    @Override
    public ResidentDiet save(ResidentDiet residentDiet) {
        return toDomain(repository.save(toEntity(residentDiet)));
    }

    private ResidentDiet toDomain(ResidentDietEntity entity) {
        return new ResidentDiet(entity.getId(), entity.getResidentId(), entity.getDietType(), entity.getObservations(), entity.getUpdatedAt());
    }

    private ResidentDietEntity toEntity(ResidentDiet domain) {
        ResidentDietEntity entity = new ResidentDietEntity();
        entity.setId(domain.id());
        entity.setResidentId(domain.residentId());
        entity.setDietType(domain.dietType());
        entity.setObservations(domain.observations());
        entity.setUpdatedAt(domain.updatedAt());
        return entity;
    }
}
