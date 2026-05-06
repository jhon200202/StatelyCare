package com.statelycare.erp.nutrition.infrastructure.persistence;

import com.statelycare.erp.nutrition.domain.model.DailyMenu;
import com.statelycare.erp.nutrition.domain.repository.DailyMenuRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JpaDailyMenuRepositoryImpl implements DailyMenuRepository {

    private final SpringDataDailyMenuRepository repository;
    
    @PersistenceContext
    private EntityManager entityManager;

    public JpaDailyMenuRepositoryImpl(SpringDataDailyMenuRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<DailyMenu> findByMenuDate(LocalDate date) {
        return repository.findAll().stream()
                .filter(e -> e.getMenuDate() != null && e.getMenuDate().equals(date))
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public DailyMenu save(DailyMenu dailyMenu) {
        UUID id = dailyMenu.id();
        Optional<DailyMenuEntity> existing = repository.findById(id);
        
        DailyMenuEntity entity = toEntity(dailyMenu);
        
        if (existing.isEmpty()) {
            entityManager.persist(entity);
        } else {
            entity = entityManager.merge(entity);
        }
        
        return toDomain(entity);
    }

    private DailyMenu toDomain(DailyMenuEntity entity) {
        return new DailyMenu(
            entity.getId(), 
            entity.getMenuDate(), 
            entity.getMenuItemId(), 
            entity.getMealType(), 
            entity.getServingsPlanned(), 
            entity.getServingsActual(), 
            entity.getNotes(),
            entity.getResidentId()
        );
    }

    private DailyMenuEntity toEntity(DailyMenu domain) {
        DailyMenuEntity entity = new DailyMenuEntity();
        entity.setId(domain.id());
        entity.setMenuDate(domain.menuDate());
        entity.setMenuItemId(domain.menuItemId());
        entity.setMealType(domain.mealType());
        entity.setServingsPlanned(domain.servingsPlanned());
        entity.setServingsActual(domain.servingsActual());
        entity.setNotes(domain.notes());
        entity.setResidentId(domain.residentId());
        return entity;
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
