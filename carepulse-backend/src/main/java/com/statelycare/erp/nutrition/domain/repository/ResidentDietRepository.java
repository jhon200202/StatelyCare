package com.statelycare.erp.nutrition.domain.repository;

import com.statelycare.erp.nutrition.domain.model.ResidentDiet;
import java.util.Optional;
import java.util.UUID;

public interface ResidentDietRepository {
    Optional<ResidentDiet> findByResidentId(UUID residentId);
    ResidentDiet save(ResidentDiet residentDiet);
}
