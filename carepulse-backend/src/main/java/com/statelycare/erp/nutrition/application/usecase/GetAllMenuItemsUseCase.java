package com.statelycare.erp.nutrition.application.usecase;

import com.statelycare.erp.nutrition.application.dto.MenuItemResponse;
import com.statelycare.erp.nutrition.domain.repository.MenuItemRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetAllMenuItemsUseCase {

    private final MenuItemRepository menuItemRepository;

    public GetAllMenuItemsUseCase(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public List<MenuItemResponse> execute() {
        return menuItemRepository.findAll().stream()
                .map(i -> new MenuItemResponse(
                    i.id(), i.name(), i.description(), i.mealType(), i.textureModification(), i.isActive()
                ))
                .collect(Collectors.toList());
    }
}
