package com.statelycare.erp.finance.infrastructure.persistence;

import com.statelycare.erp.finance.domain.model.Invoice;
import com.statelycare.erp.finance.domain.repository.InvoiceRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JpaInvoiceRepositoryImpl implements InvoiceRepository {

    private final SpringDataInvoiceRepository repository;

    public JpaInvoiceRepositoryImpl(SpringDataInvoiceRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Invoice> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "dueDate")).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Invoice> findByResidentId(UUID residentId) {
        return repository.findByResidentIdOrderByDueDateDesc(residentId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Invoice> findByStatus(String status) {
        return repository.findByStatusOrderByDueDateAsc(status).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Invoice> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Invoice save(Invoice invoice) {
        return toDomain(repository.save(toEntity(invoice)));
    }

    private Invoice toDomain(InvoiceEntity entity) {
        return new Invoice(
            entity.getId(), entity.getResidentId(), entity.getAmount(),
            entity.getIssueDate(), entity.getDueDate(), entity.getStatus(),
            entity.getDescription(), entity.getCreatedAt(), entity.getUpdatedAt()
        );
    }

    private InvoiceEntity toEntity(Invoice domain) {
        InvoiceEntity entity = new InvoiceEntity();
        entity.setId(domain.id());
        entity.setResidentId(domain.residentId());
        entity.setAmount(domain.amount());
        entity.setIssueDate(domain.issueDate());
        entity.setDueDate(domain.dueDate());
        entity.setStatus(domain.status());
        entity.setDescription(domain.description());
        entity.setCreatedAt(domain.createdAt());
        entity.setUpdatedAt(domain.updatedAt());
        return entity;
    }
}
