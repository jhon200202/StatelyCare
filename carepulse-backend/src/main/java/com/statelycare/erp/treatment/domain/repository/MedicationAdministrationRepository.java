package com.statelycare.erp.treatment.domain.repository;

import com.statelycare.erp.treatment.domain.model.MedicationAdministration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MedicationAdministrationRepository {
    List<MedicationAdministration> findAll();
    Optional<MedicationAdministration> findById(UUID id);
    MedicationAdministration save(MedicationAdministration administration);
    void deleteById(UUID id);
}
