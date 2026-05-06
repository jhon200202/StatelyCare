package com.statelycare.erp.employee.infrastructure.persistence;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataEmployeeRepository extends JpaRepository<EmployeeEntity, UUID> {
    Optional<EmployeeEntity> findByUserId(UUID userId);
    List<EmployeeEntity> findByDeletedAtIsNull();

    @Query("SELECT e FROM EmployeeEntity e WHERE e.deletedAt IS NULL AND " +
           "(:query IS NULL OR :query = '' OR " +
           "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<EmployeeEntity> searchEmployees(@Param("query") String query, Sort sort);
}
