package com.statelycare.erp.treatment.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.statelycare.erp.treatment.domain.model.TreatmentStatus;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataTreatmentRepository extends JpaRepository<TreatmentEntity, UUID> {
    List<TreatmentEntity> findByResidentIdAndStatus(UUID residentId, TreatmentStatus status);
    List<TreatmentEntity> findByDeletedAtIsNull();
}
