package com.statelycare.erp.nutrition.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataDailyMenuRepository extends JpaRepository<DailyMenuEntity, UUID> {
    List<DailyMenuEntity> findByMenuDate(LocalDate menuDate);  
}
