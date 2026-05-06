package com.statelycare.erp.resident.infrastructure.persistence;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataResidentRepository extends JpaRepository<ResidentEntity, UUID> {
    List<ResidentEntity> findByDeletedAtIsNull();

    @Query("SELECT r FROM ResidentEntity r WHERE r.deletedAt IS NULL AND " +
           "(:query IS NULL OR :query = '' OR " +
           "LOWER(r.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(r.lastName) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<ResidentEntity> searchResidents(@Param("query") String query, Sort sort);
}
