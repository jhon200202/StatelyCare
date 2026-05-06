package com.statelycare.erp.finance.application.usecase;

import com.statelycare.erp.finance.application.dto.PaymentResponse;
import com.statelycare.erp.finance.application.dto.RegisterPaymentRequest;
import com.statelycare.erp.finance.domain.model.Invoice;
import com.statelycare.erp.finance.domain.model.Payment;
import com.statelycare.erp.finance.domain.repository.InvoiceRepository;
import com.statelycare.erp.finance.domain.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;

    public RegisterPaymentUseCase(PaymentRepository paymentRepository, InvoiceRepository invoiceRepository) {
        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Transactional
    public PaymentResponse execute(RegisterPaymentRequest request) {
        Invoice invoice = invoiceRepository.findById(request.invoiceId())
            .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));

        if (!invoice.status().equals("PENDING") && !invoice.status().equals("OVERDUE")) {
            throw new IllegalStateException("Invoice is already paid or in an invalid state");
        }

        Payment newPayment = Payment.register(
            request.invoiceId(),
            request.residentId(),
            request.amountPaid(),
            request.paymentMethod()
        );

        Payment savedPayment = paymentRepository.save(newPayment);

        // Actualizar factura a pagada
        // En un sistema real verificaríamos si el monto pagado cubre la totalidad.
        Invoice paidInvoice = invoice.markAsPaid();
        invoiceRepository.save(paidInvoice);

        return new PaymentResponse(
            savedPayment.id(), savedPayment.invoiceId(), savedPayment.residentId(),
            savedPayment.amountPaid(), savedPayment.paymentDate(), savedPayment.paymentMethod()
        );
    }
}
