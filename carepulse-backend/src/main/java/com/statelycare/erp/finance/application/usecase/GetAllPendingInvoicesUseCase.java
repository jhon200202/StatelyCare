package com.statelycare.erp.finance.application.usecase;

import com.statelycare.erp.finance.application.dto.InvoiceResponse;
import com.statelycare.erp.finance.domain.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetAllPendingInvoicesUseCase {

    private final InvoiceRepository invoiceRepository;

    public GetAllPendingInvoicesUseCase(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public List<InvoiceResponse> execute() {
        return invoiceRepository.findByStatus("PENDING").stream()
            .map(i -> new InvoiceResponse(
                i.id(), i.residentId(), i.amount(),
                i.issueDate(), i.dueDate(), i.status(),
                i.description()
            ))
            .collect(Collectors.toList());
    }
}
