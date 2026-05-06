package com.statelycare.erp.treatment.application.usecase;

import com.statelycare.erp.treatment.application.dto.TreatmentResponse;
import com.statelycare.erp.treatment.domain.repository.TreatmentRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GetActiveTreatmentsByResidentUseCase {

    private final TreatmentRepository repository;

    public GetActiveTreatmentsByResidentUseCase(TreatmentRepository repository) {
        this.repository = repository;
    }

    public List<TreatmentResponse> execute(UUID residentId) {
        return repository.findActiveByResidentId(residentId).stream()
                .map(t -> new TreatmentResponse(
                    t.id(), t.residentId(), t.prescribedBy(), t.treatmentName(),
                    t.treatmentType(), t.frequency(), t.scheduledTime(),
                    t.startDate(), t.endDate(), t.status()
                ))
                .collect(Collectors.toList());
    }
}
