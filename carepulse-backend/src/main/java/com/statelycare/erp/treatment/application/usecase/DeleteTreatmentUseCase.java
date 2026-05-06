package com.statelycare.erp.treatment.application.usecase;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.statelycare.erp.treatment.domain.repository.TreatmentRepository;

@Service
public class DeleteTreatmentUseCase {

    private final TreatmentRepository repository;

    public DeleteTreatmentUseCase(TreatmentRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void execute(UUID id) {
        repository.deleteById(id);
    }
}