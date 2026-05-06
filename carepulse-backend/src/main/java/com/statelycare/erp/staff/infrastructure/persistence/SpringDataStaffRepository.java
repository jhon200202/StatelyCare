package com.statelycare.erp.staff.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataStaffRepository extends JpaRepository<StaffEntity, UUID> {
    List<StaffEntity> findByDeletedAtIsNull();
}
