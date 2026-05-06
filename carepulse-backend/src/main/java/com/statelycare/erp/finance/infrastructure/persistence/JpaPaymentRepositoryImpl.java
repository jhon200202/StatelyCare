package com.statelycare.erp.finance.infrastructure.persistence;

import com.statelycare.erp.finance.domain.model.Payment;
import com.statelycare.erp.finance.domain.repository.PaymentRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JpaPaymentRepositoryImpl implements PaymentRepository {

    private final SpringDataPaymentRepository repository;

    public JpaPaymentRepositoryImpl(SpringDataPaymentRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Payment> findByInvoiceId(UUID invoiceId) {
        return repository.findByInvoiceIdOrderByPaymentDateDesc(invoiceId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Payment> findAll() {
        return repository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public Payment save(Payment payment) {
        return toDomain(repository.save(toEntity(payment)));
    }

    private Payment toDomain(PaymentEntity entity) {
        return new Payment(
            entity.getId(), entity.getInvoiceId(), entity.getResidentId(),
            entity.getAmountPaid(), entity.getPaymentDate(), entity.getPaymentMethod()
        );
    }

    private PaymentEntity toEntity(Payment domain) {
        PaymentEntity entity = new PaymentEntity();
        entity.setId(domain.id());
        entity.setInvoiceId(domain.invoiceId());
        entity.setResidentId(domain.residentId());
        entity.setAmountPaid(domain.amountPaid());
        entity.setPaymentDate(domain.paymentDate());
        entity.setPaymentMethod(domain.paymentMethod());
        return entity;
    }
}
