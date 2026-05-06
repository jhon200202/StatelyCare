package com.statelycare.erp.nutrition.application.usecase;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.statelycare.erp.nutrition.application.dto.DailyMenuItemResponse;
import com.statelycare.erp.nutrition.domain.model.DailyMenu;
import com.statelycare.erp.nutrition.domain.repository.DailyMenuRepository;

@Service
public class GetMenuByDateUseCase {

    private final DailyMenuRepository repository;

    public GetMenuByDateUseCase(DailyMenuRepository repository) {
        this.repository = repository;
    }

    public List<DailyMenuItemResponse> execute(String date) {
        LocalDate localDate = LocalDate.parse(date);
        return repository.findByMenuDate(localDate)
            .stream()
            .map(this::mapToResponse)
            .toList();
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
