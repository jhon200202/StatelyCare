package com.statelycare.erp.finance.application.usecase;

import com.statelycare.erp.finance.application.dto.TransactionResponse;
import com.statelycare.erp.finance.domain.repository.FinancialTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetTransactionsUseCase {

    private final FinancialTransactionRepository transactionRepository;

    public GetTransactionsUseCase(FinancialTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<TransactionResponse> execute(String category, String status) {
        var transactions = (category != null || status != null)
                ? transactionRepository.findByFilters(category, status)
                : transactionRepository.findAll();

        return transactions.stream()
                .map(t -> new TransactionResponse(
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
                ))
                .collect(Collectors.toList());
    }
}