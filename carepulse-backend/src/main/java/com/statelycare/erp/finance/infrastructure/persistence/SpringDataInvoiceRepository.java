package com.statelycare.erp.finance.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataInvoiceRepository extends JpaRepository<InvoiceEntity, UUID> {
    List<InvoiceEntity> findByResidentIdOrderByDueDateDesc(UUID residentId);
    List<InvoiceEntity> findByStatusOrderByDueDateAsc(String status);
}
