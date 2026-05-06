package com.statelycare.erp.nutrition.application.usecase;

import com.statelycare.erp.nutrition.application.dto.MenuItemRequest;
import com.statelycare.erp.nutrition.application.dto.MenuItemResponse;
import com.statelycare.erp.nutrition.domain.model.MenuItem;
import com.statelycare.erp.nutrition.domain.repository.MenuItemRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateMenuItemUseCase {

    private final MenuItemRepository repository;

    public CreateMenuItemUseCase(MenuItemRepository repository) {
        this.repository = repository;
    }

    public MenuItemResponse execute(MenuItemRequest request) {
        MenuItem item = MenuItem.create(
            request.name(),
            request.description(),
            request.mealType(),
            request.textureModification()
        );

        MenuItem saved = repository.save(item);
        return mapToResponse(saved);
    }

    private MenuItemResponse mapToResponse(MenuItem m) {
        return new MenuItemResponse(
            m.id(), m.name(), m.description(), m.mealType(), m.textureModification(), m.isActive()
        );
    }
}
