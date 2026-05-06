package com.statelycare.erp.nutrition.infrastructure.persistence;

import com.statelycare.erp.nutrition.domain.model.MenuItem;
import com.statelycare.erp.nutrition.domain.repository.MenuItemRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JpaMenuItemRepositoryImpl implements MenuItemRepository {

    private final SpringDataMenuItemRepository repository;

    public JpaMenuItemRepositoryImpl(SpringDataMenuItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<MenuItem> findAll() {
        return repository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<MenuItem> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public MenuItem save(MenuItem menuItem) {
        return toDomain(repository.save(toEntity(menuItem)));
    }

    private MenuItem toDomain(MenuItemEntity entity) {
        return new MenuItem(
            entity.getId(), 
            entity.getName(), 
            entity.getDescription(), 
            entity.getMealType(), 
            entity.getTextureModification(), 
            entity.isActive()
        );
    }

    private MenuItemEntity toEntity(MenuItem domain) {
        MenuItemEntity entity = new MenuItemEntity();
        entity.setId(domain.id());
        entity.setName(domain.name());
        entity.setDescription(domain.description());
        entity.setMealType(domain.mealType());
        entity.setTextureModification(domain.textureModification());
        entity.setActive(domain.isActive());
        return entity;
    }
}
