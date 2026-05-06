package com.statelycare.erp.treatment.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataMedicationLogRepository extends JpaRepository<MedicationLogEntity, UUID> {
    List<MedicationLogEntity> findByResidentIdOrderByAdministeredAtDesc(UUID residentId);
    List<MedicationLogEntity> findByTreatmentIdOrderByAdministeredAtDesc(UUID treatmentId);
}
