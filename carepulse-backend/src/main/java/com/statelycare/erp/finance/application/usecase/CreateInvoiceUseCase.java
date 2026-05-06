package com.statelycare.erp.finance.application.usecase;

import com.statelycare.erp.finance.application.dto.CreateInvoiceRequest;
import com.statelycare.erp.finance.application.dto.InvoiceResponse;
import com.statelycare.erp.finance.domain.model.Invoice;
import com.statelycare.erp.finance.domain.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateInvoiceUseCase {

    private final InvoiceRepository invoiceRepository;

    public CreateInvoiceUseCase(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public InvoiceResponse execute(CreateInvoiceRequest request) {
        Invoice newInvoice = Invoice.createNew(
            request.residentId(),
            request.amount(),
            request.issueDate(),
            request.dueDate(),
            request.description()
        );

        Invoice saved = invoiceRepository.save(newInvoice);

        return new InvoiceResponse(
            saved.id(), saved.residentId(), saved.amount(),
            saved.issueDate(), saved.dueDate(), saved.status(),
            saved.description()
        );
    }
}
