package com.statelycare.erp.nutrition.domain.repository;

import com.statelycare.erp.nutrition.domain.model.MenuItem;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuItemRepository {
    List<MenuItem> findAll();
    Optional<MenuItem> findById(UUID id);
    MenuItem save(MenuItem menuItem);
}
