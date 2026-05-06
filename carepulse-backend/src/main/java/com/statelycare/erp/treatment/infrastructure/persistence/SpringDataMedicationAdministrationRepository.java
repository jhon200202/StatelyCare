package com.statelycare.erp.treatment.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface SpringDataMedicationAdministrationRepository extends JpaRepository<MedicationAdministrationEntity, UUID> {}
