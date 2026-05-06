package com.statelycare.erp.treatment.domain.repository;

import com.statelycare.erp.treatment.domain.model.Treatment;
import com.statelycare.erp.treatment.domain.model.TreatmentStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TreatmentRepository {
    List<Treatment> findActiveByResidentId(UUID residentId);
    List<Treatment> findByResidentIdAndStatus(UUID residentId, TreatmentStatus status);
    Optional<Treatment> findById(UUID id);
    Treatment save(Treatment treatment);
    void deleteById(UUID id);
}
