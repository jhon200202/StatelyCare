package com.statelycare.erp.finance.application.usecase;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.statelycare.erp.finance.domain.repository.FinancialTransactionRepository;

@Component
public class SoftDeleteTransactionUseCase {

    private final FinancialTransactionRepository repository;

    public SoftDeleteTransactionUseCase(FinancialTransactionRepository repository) {
        this.repository = repository;
    }

    public void execute(UUID id) {
        repository.deleteById(id);
    }
}