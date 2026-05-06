package com.statelycare.erp.nutrition.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataResidentDietRepository extends JpaRepository<ResidentDietEntity, UUID> {
    Optional<ResidentDietEntity> findByResidentId(UUID residentId);
}
