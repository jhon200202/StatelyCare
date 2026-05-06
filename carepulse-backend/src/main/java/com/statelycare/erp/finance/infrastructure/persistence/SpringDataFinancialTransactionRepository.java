package com.statelycare.erp.finance.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataFinancialTransactionRepository extends JpaRepository<FinancialTransactionEntity, UUID> {
    
    List<FinancialTransactionEntity> findByDeletedAtIsNull();
    
    Optional<FinancialTransactionEntity> findByIdAndDeletedAtIsNull(UUID id);
}