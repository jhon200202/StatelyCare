package com.statelycare.erp.finance.infrastructure.persistence;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class PaymentEntity {
    
    @Id
    private UUID id;
    
    @Column(nullable = false, name = "invoice_id")
    private UUID invoiceId;
    
    @Column(nullable = false, name = "resident_id")
    private UUID residentId;
    
    @Column(nullable = false, name = "amount_paid", precision = 10, scale = 2)
    private BigDecimal amountPaid;
    
    @Column(nullable = false, name = "payment_date")
    private Instant paymentDate;
    
    @Column(nullable = false, name = "payment_method")
    private String paymentMethod;

    public PaymentEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getInvoiceId() { return invoiceId; }
    public void setInvoiceId(UUID invoiceId) { this.invoiceId = invoiceId; }
    public UUID getResidentId() { return residentId; }
    public void setResidentId(UUID residentId) { this.residentId = residentId; }
    public BigDecimal getAmountPaid() { return amountPaid; }
    public void setAmountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; }
    public Instant getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Instant paymentDate) { this.paymentDate = paymentDate; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
