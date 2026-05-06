package com.statelycare.erp.nutrition.domain.repository;

import com.statelycare.erp.nutrition.domain.model.DailyMenu;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface DailyMenuRepository {
    List<DailyMenu> findByMenuDate(LocalDate date);
    DailyMenu save(DailyMenu dailyMenu);
    void delete(UUID id);
}
