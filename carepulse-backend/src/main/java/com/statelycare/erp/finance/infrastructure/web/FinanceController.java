package com.statelycare.erp.finance.infrastructure.web;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.statelycare.erp.finance.application.dto.TransactionRequest;
import com.statelycare.erp.finance.application.dto.TransactionResponse;
import com.statelycare.erp.finance.application.usecase.CreateTransactionUseCase;
import com.statelycare.erp.finance.application.usecase.GetTransactionsUseCase;
import com.statelycare.erp.finance.application.usecase.SoftDeleteTransactionUseCase;
import com.statelycare.erp.finance.application.usecase.UpdateTransactionUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/finance")
public class FinanceController {

    private final CreateTransactionUseCase createTransactionUseCase;
    private final GetTransactionsUseCase getTransactionsUseCase;
    private final UpdateTransactionUseCase updateTransactionUseCase;
    private final SoftDeleteTransactionUseCase softDeleteTransactionUseCase;

    public FinanceController(CreateTransactionUseCase createTransactionUseCase,
                             GetTransactionsUseCase getTransactionsUseCase,
                             UpdateTransactionUseCase updateTransactionUseCase,
                             SoftDeleteTransactionUseCase softDeleteTransactionUseCase) {
        this.createTransactionUseCase = createTransactionUseCase;
        this.getTransactionsUseCase = getTransactionsUseCase;
        this.updateTransactionUseCase = updateTransactionUseCase;
        this.softDeleteTransactionUseCase = softDeleteTransactionUseCase;
    }

    @PostMapping("/transactions")
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody TransactionRequest request) {
        return new ResponseEntity<>(createTransactionUseCase.execute(request), HttpStatus.CREATED);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactions(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(getTransactionsUseCase.execute(category, status));
    }

    @PutMapping("/transactions/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @PathVariable UUID id,
            @Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.ok(updateTransactionUseCase.execute(id, request));
    }

    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable UUID id) {
        softDeleteTransactionUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}