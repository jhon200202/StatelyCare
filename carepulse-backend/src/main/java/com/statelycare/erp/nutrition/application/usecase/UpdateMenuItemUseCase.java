package com.statelycare.erp.nutrition.application.usecase;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.statelycare.erp.nutrition.application.dto.MenuItemRequest;
import com.statelycare.erp.nutrition.application.dto.MenuItemResponse;
import com.statelycare.erp.nutrition.domain.model.MenuItem;
import com.statelycare.erp.nutrition.domain.repository.MenuItemRepository;

@Service
public class UpdateMenuItemUseCase {

    private final MenuItemRepository repository;

    public UpdateMenuItemUseCase(MenuItemRepository repository) {
        this.repository = repository;
    }

    public MenuItemResponse execute(UUID id, MenuItemRequest request) {
        MenuItem existing = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Menu item not found"));
        
        MenuItem updated = new MenuItem(
            existing.id(),
            request.name(),
            request.description(),
            request.mealType(),
            request.textureModification(),
            existing.isActive()
        );

        MenuItem saved = repository.save(updated);
        return mapToResponse(saved);
    }

    private MenuItemResponse mapToResponse(MenuItem m) {
        return new MenuItemResponse(
            m.id(), m.name(), m.description(), m.mealType(), m.textureModification(), m.isActive()
        );
    }
}
