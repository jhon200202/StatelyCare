package com.statelycare.erp.finance.domain.repository;

import com.statelycare.erp.finance.domain.model.Payment;
import java.util.List;
import java.util.UUID;

public interface PaymentRepository {
    List<Payment> findByInvoiceId(UUID invoiceId);
    List<Payment> findAll();
    Payment save(Payment payment);
}
