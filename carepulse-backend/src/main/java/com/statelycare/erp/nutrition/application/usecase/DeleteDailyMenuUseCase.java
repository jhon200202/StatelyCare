package com.statelycare.erp.nutrition.application.usecase;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.statelycare.erp.nutrition.domain.repository.DailyMenuRepository;

@Service
public class DeleteDailyMenuUseCase {

    private final DailyMenuRepository repository;

    public DeleteDailyMenuUseCase(DailyMenuRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void execute(UUID id) {
        repository.delete(id);
    }
}