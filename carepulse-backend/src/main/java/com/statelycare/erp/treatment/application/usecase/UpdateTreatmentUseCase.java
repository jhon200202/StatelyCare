package com.statelycare.erp.treatment.application.usecase;

import com.statelycare.erp.treatment.application.dto.TreatmentRequest;
import com.statelycare.erp.treatment.application.dto.TreatmentResponse;
import com.statelycare.erp.treatment.domain.model.Treatment;
import com.statelycare.erp.treatment.domain.repository.TreatmentRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.UUID;

@Service
public class UpdateTreatmentUseCase {

    private final TreatmentRepository repository;

    public UpdateTreatmentUseCase(TreatmentRepository repository) {
        this.repository = repository;
    }

    public TreatmentResponse execute(UUID id, TreatmentRequest request) {
        Treatment existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Treatment not found"));

        Treatment updated = new Treatment(
            existing.id(),
            request.residentId(),
            request.prescribedBy(),
            request.treatmentName(),
            request.treatmentType(),
            existing.description(), // Keeping existing or could add to request
            request.frequency(),
            existing.scheduledTime(), // Keeping existing or could add to request
            request.startDate(),
            existing.endDate(), // Keeping existing or could add to request
            existing.status(), // Keep status or could add to request
            existing.instructions(), // Keeping existing or could add to request
            existing.createdAt(),
            Instant.now()
        );

        Treatment saved = repository.save(updated);
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
