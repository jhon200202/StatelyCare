package com.statelycare.erp.finance.application.usecase;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.statelycare.erp.finance.application.dto.TransactionRequest;
import com.statelycare.erp.finance.application.dto.TransactionResponse;
import com.statelycare.erp.finance.domain.model.FinancialTransaction;
import com.statelycare.erp.finance.domain.repository.FinancialTransactionRepository;

@Component
public class UpdateTransactionUseCase {

    private final FinancialTransactionRepository repository;

    public UpdateTransactionUseCase(FinancialTransactionRepository repository) {
        this.repository = repository;
    }

    public TransactionResponse execute(UUID id, TransactionRequest request) {
        FinancialTransaction existing = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Transaction not found: " + id));

       
        FinancialTransaction updated = new FinancialTransaction(
            existing.id(),
            existing.transactionCode(),
            request.transactionType(),
            request.category(),
            request.amount(),
            existing.currency(),
            request.description(),
            request.residentId(),
            request.vendorName(),
            request.invoiceNumber(),
            request.paymentMethod(),
            existing.status(),
            request.transactionDate(),
            existing.dueDate(),
            existing.settledDate(),
            existing.createdBy(),
            existing.createdAt(),
            Instant.now()
        );

        FinancialTransaction saved = repository.save(updated);
        return toResponse(saved);
    }

    private TransactionResponse toResponse(FinancialTransaction t) {
        return new TransactionResponse(
            t.id(),
            t.transactionCode(),
            t.transactionType(),
            t.category(),
            t.amount(),
            t.currency(),
            t.description(),
            t.residentId(),
            t.vendorName(),
            t.invoiceNumber(),
            t.paymentMethod(),
            t.status(),
            t.transactionDate()
        );
    }
}