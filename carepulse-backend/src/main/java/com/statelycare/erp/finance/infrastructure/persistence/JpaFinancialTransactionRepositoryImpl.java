package com.statelycare.erp.finance.infrastructure.persistence;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.statelycare.erp.finance.domain.model.FinancialTransaction;
import com.statelycare.erp.finance.domain.repository.FinancialTransactionRepository;

@Component
public class JpaFinancialTransactionRepositoryImpl implements FinancialTransactionRepository {

    private final SpringDataFinancialTransactionRepository repository;

    public JpaFinancialTransactionRepositoryImpl(SpringDataFinancialTransactionRepository repository) {
        this.repository = repository;
    }

    
    @Override
    public List<FinancialTransaction> findAll() {
        return repository.findByDeletedAtIsNull().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
public List<FinancialTransaction> findByFilters(String category, String status) {
    return repository.findByDeletedAtIsNull().stream()
            .filter(e -> category == null || e.getCategory().name().equalsIgnoreCase(category))
            .filter(e -> status == null || e.getStatus().name().equalsIgnoreCase(status))
            .map(this::toDomain)
            .collect(Collectors.toList());
}

    @Override
    public Optional<FinancialTransaction> findById(UUID id) {
        return repository.findByIdAndDeletedAtIsNull(id).map(this::toDomain);
    }

    
    @Override
    public FinancialTransaction save(FinancialTransaction transaction) {
        return toDomain(repository.save(toEntity(transaction)));
    }

    @Override
    public void deleteById(UUID id) {
        repository.findById(id).ifPresent(entity -> {
            entity.setDeletedAt(Instant.now());
            repository.save(entity);
        });
    }

    private FinancialTransaction toDomain(FinancialTransactionEntity entity) {
        return new FinancialTransaction(
            entity.getId(),
            entity.getTransactionCode(),
            entity.getTransactionType(),
            entity.getCategory(),
            entity.getAmount(),
            entity.getCurrency(),
            entity.getDescription(),
            entity.getResidentId(),
            entity.getVendorName(),
            entity.getInvoiceNumber(),
            entity.getPaymentMethod(),
            entity.getStatus(),
            entity.getTransactionDate(),
            entity.getDueDate(),
            entity.getSettledDate(),
            entity.getCreatedBy(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    private FinancialTransactionEntity toEntity(FinancialTransaction domain) {
        FinancialTransactionEntity entity = new FinancialTransactionEntity();
        entity.setId(domain.id());
        entity.setTransactionCode(domain.transactionCode());
        entity.setTransactionType(domain.transactionType());
        entity.setCategory(domain.category());
        entity.setAmount(domain.amount());
        entity.setCurrency(domain.currency());
        entity.setDescription(domain.description());
        entity.setResidentId(domain.residentId());
        entity.setVendorName(domain.vendorName());
        entity.setInvoiceNumber(domain.invoiceNumber());
        entity.setPaymentMethod(domain.paymentMethod());
        entity.setStatus(domain.status());
        entity.setTransactionDate(domain.transactionDate());
        entity.setDueDate(domain.dueDate());
        entity.setSettledDate(domain.settledDate());
        entity.setCreatedBy(domain.createdBy());
        entity.setCreatedAt(domain.createdAt());
        entity.setUpdatedAt(domain.updatedAt());
        return entity;
    }
}
