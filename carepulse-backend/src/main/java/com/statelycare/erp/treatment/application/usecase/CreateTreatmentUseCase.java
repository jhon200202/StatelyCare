package com.statelycare.erp.treatment.application.usecase;

import com.statelycare.erp.treatment.application.dto.TreatmentRequest;
import com.statelycare.erp.treatment.application.dto.TreatmentResponse;
import com.statelycare.erp.treatment.domain.model.Treatment;
import com.statelycare.erp.treatment.domain.repository.TreatmentRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateTreatmentUseCase {

    private final TreatmentRepository repository;

    public CreateTreatmentUseCase(TreatmentRepository repository) {
        this.repository = repository;
    }

    public TreatmentResponse execute(TreatmentRequest request) {
        Treatment treatment = Treatment.createNew(
            request.residentId(),
            request.prescribedBy(),
            request.treatmentName(),
            request.treatmentType(),
            request.frequency(),
            request.startDate()
        );

        Treatment saved = repository.save(treatment);
        return mapToResponse(saved);
    }

    private TreatmentResponse mapToResponse(Treatment t) {
        return new TreatmentResponse(
            t.id(), t.residentId(), t.prescribedBy(), t.treatmentName(),
            t.treatmentType(), t.frequency(), t.scheduledTime(),
            t.startDate(), t.endDate(), t.status()
        );
    }
}
