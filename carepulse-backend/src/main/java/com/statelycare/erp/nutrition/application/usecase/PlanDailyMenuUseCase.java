package com.statelycare.erp.nutrition.application.usecase;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.statelycare.erp.nutrition.application.dto.DailyMenuItemResponse;
import com.statelycare.erp.nutrition.application.dto.DailyMenuRequest;
import com.statelycare.erp.nutrition.domain.model.DailyMenu;
import com.statelycare.erp.nutrition.domain.repository.DailyMenuRepository;

@Service
public class PlanDailyMenuUseCase {

    private final DailyMenuRepository repository;

    public PlanDailyMenuUseCase(DailyMenuRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public DailyMenuItemResponse execute(DailyMenuRequest request) {
        LocalDate date = LocalDate.parse(request.date());
        
        DailyMenu menu = DailyMenu.create(
            date,
            request.menuItemId(),
            request.mealType(),
            request.servingsPlanned(),
            request.residentId()
        );

        DailyMenu saved = repository.save(menu);
        return mapToResponse(saved);
    }

    private DailyMenuItemResponse mapToResponse(DailyMenu m) {
        return new DailyMenuItemResponse(
            m.id().toString(),
            m.menuDate() != null ? m.menuDate().toString() : "",
            m.mealType().toString(),
            m.menuItemId().toString(),
            m.servingsPlanned(),
            m.notes() != null ? m.notes() : "",
            m.residentId() != null ? m.residentId().toString() : null
        );
    }
}
