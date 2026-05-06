package com.statelycare.erp.finance.application.usecase;

import com.statelycare.erp.finance.application.dto.TransactionRequest;
import com.statelycare.erp.finance.application.dto.TransactionResponse;
import com.statelycare.erp.finance.domain.model.FinancialTransaction;
import com.statelycare.erp.finance.domain.repository.FinancialTransactionRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class CreateTransactionUseCase {

    private final FinancialTransactionRepository repository;

    public CreateTransactionUseCase(FinancialTransactionRepository repository) {
        this.repository = repository;
    }

    public TransactionResponse execute(TransactionRequest request) {
        String txCode = "TX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        FinancialTransaction transaction = FinancialTransaction.createNew(
            txCode,
            request.transactionType(),
            request.category(),
            request.amount(),
            request.paymentMethod(),
            request.transactionDate()
        );

        FinancialTransaction saved = repository.save(transaction);
        return mapToResponse(saved);
    }

    private TransactionResponse mapToResponse(FinancialTransaction t) {
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
