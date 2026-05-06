package com.statelycare.erp.finance.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.statelycare.erp.finance.domain.model.FinancialTransaction;


public interface FinancialTransactionRepository {
    List<FinancialTransaction> findAll();
    List<FinancialTransaction> findByFilters(String category, String status);
    Optional<FinancialTransaction> findById(UUID id);
    FinancialTransaction save(FinancialTransaction transaction);
    void deleteById(UUID id);
}
