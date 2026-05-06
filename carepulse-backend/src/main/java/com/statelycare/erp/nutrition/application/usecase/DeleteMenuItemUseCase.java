package com.statelycare.erp.nutrition.application.usecase;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.statelycare.erp.nutrition.domain.model.MenuItem;
import com.statelycare.erp.nutrition.domain.repository.MenuItemRepository;

@Service
public class DeleteMenuItemUseCase {

    private final MenuItemRepository repository;

    public DeleteMenuItemUseCase(MenuItemRepository repository) {
        this.repository = repository;
    }

    public void execute(UUID id) {
        MenuItem existing = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Menu item not found"));
        
        MenuItem deleted = new MenuItem(
            existing.id(),
            existing.name(),
            existing.description(),
            existing.mealType(),
            existing.textureModification(),
            false
        );

        repository.save(deleted);
    }
}
