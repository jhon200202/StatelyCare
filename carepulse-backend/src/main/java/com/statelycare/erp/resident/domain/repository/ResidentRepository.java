package com.statelycare.erp.resident.domain.repository;

import com.statelycare.erp.resident.domain.model.Resident;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResidentRepository {
    List<Resident> findAll();
    List<Resident> findAll(String query, String sortBy, String direction);
    Optional<Resident> findById(UUID id);
    Resident save(Resident resident);
    void delete(Resident resident); 
    long count();
}
