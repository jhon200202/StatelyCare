package com.statelycare.erp.treatment.domain.repository;

import com.statelycare.erp.treatment.domain.model.MedicationLog;
import java.util.List;
import java.util.UUID;

public interface MedicationLogRepository {
    List<MedicationLog> findByResidentId(UUID residentId);
    List<MedicationLog> findByTreatmentId(UUID treatmentId);
    MedicationLog save(MedicationLog log);
}
