package com.statelycare.erp.finance.domain.repository;

import com.statelycare.erp.finance.domain.model.Invoice;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvoiceRepository {
    List<Invoice> findAll();
    List<Invoice> findByResidentId(UUID residentId);
    List<Invoice> findByStatus(String status);
    Optional<Invoice> findById(UUID id);
    Invoice save(Invoice invoice);
}
