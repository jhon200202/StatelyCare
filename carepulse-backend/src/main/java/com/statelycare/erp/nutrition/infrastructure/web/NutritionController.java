package com.statelycare.erp.nutrition.infrastructure.web;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.statelycare.erp.nutrition.application.dto.DailyMenuItemResponse;
import com.statelycare.erp.nutrition.application.dto.DailyMenuRequest;
import com.statelycare.erp.nutrition.application.dto.MenuItemRequest;
import com.statelycare.erp.nutrition.application.dto.MenuItemResponse;
import com.statelycare.erp.nutrition.application.usecase.CreateMenuItemUseCase;
import com.statelycare.erp.nutrition.application.usecase.DeleteDailyMenuUseCase;
import com.statelycare.erp.nutrition.application.usecase.DeleteMenuItemUseCase;
import com.statelycare.erp.nutrition.application.usecase.GetAllMenuItemsUseCase;
import com.statelycare.erp.nutrition.application.usecase.GetMenuByDateUseCase;
import com.statelycare.erp.nutrition.application.usecase.PlanDailyMenuUseCase;
import com.statelycare.erp.nutrition.application.usecase.UpdateDailyMenuUseCase;
import com.statelycare.erp.nutrition.application.usecase.UpdateMenuItemUseCase;
import com.statelycare.erp.nutrition.application.usecase.GenerateDailyMenuPdfUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/nutrition")
public class NutritionController {

    private final CreateMenuItemUseCase createMenuItemUseCase;
    private final GetAllMenuItemsUseCase getAllMenuItemsUseCase;
    private final UpdateMenuItemUseCase updateMenuItemUseCase;
    private final DeleteMenuItemUseCase deleteMenuItemUseCase;
    private final PlanDailyMenuUseCase planDailyMenuUseCase;
    private final GetMenuByDateUseCase getMenuByDateUseCase;
    private final UpdateDailyMenuUseCase updateDailyMenuUseCase;
    private final DeleteDailyMenuUseCase deleteDailyMenuUseCase;
    private final GenerateDailyMenuPdfUseCase generateDailyMenuPdfUseCase;

    public NutritionController(
            CreateMenuItemUseCase createMenuItemUseCase,
            GetAllMenuItemsUseCase getAllMenuItemsUseCase,
            UpdateMenuItemUseCase updateMenuItemUseCase,
            DeleteMenuItemUseCase deleteMenuItemUseCase,
            PlanDailyMenuUseCase planDailyMenuUseCase,
            GetMenuByDateUseCase getMenuByDateUseCase,
            UpdateDailyMenuUseCase updateDailyMenuUseCase,
            DeleteDailyMenuUseCase deleteDailyMenuUseCase,
            GenerateDailyMenuPdfUseCase generateDailyMenuPdfUseCase) {
        this.createMenuItemUseCase = createMenuItemUseCase;
        this.getAllMenuItemsUseCase = getAllMenuItemsUseCase;
        this.updateMenuItemUseCase = updateMenuItemUseCase;
        this.deleteMenuItemUseCase = deleteMenuItemUseCase;
        this.planDailyMenuUseCase = planDailyMenuUseCase;
        this.getMenuByDateUseCase = getMenuByDateUseCase;
        this.updateDailyMenuUseCase = updateDailyMenuUseCase;
        this.deleteDailyMenuUseCase = deleteDailyMenuUseCase;
        this.generateDailyMenuPdfUseCase = generateDailyMenuPdfUseCase;
    }

    @GetMapping("/items")
    public ResponseEntity<List<MenuItemResponse>> getAllItems() {
        return ResponseEntity.ok(getAllMenuItemsUseCase.execute());
    }

    @PostMapping("/items")
    public ResponseEntity<MenuItemResponse> createItem(@Valid @RequestBody MenuItemRequest request) {
        return new ResponseEntity<>(createMenuItemUseCase.execute(request), HttpStatus.CREATED);
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<MenuItemResponse> updateItem(
            @PathVariable UUID id,
            @Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.ok(updateMenuItemUseCase.execute(id, request));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable UUID id) {
        deleteMenuItemUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/menu")
    public ResponseEntity<DailyMenuItemResponse> planDailyMenu(@Valid @RequestBody DailyMenuRequest request) {
        return new ResponseEntity<>(planDailyMenuUseCase.execute(request), HttpStatus.CREATED);
    }

    @GetMapping("/menu/{date}")
    public ResponseEntity<List<DailyMenuItemResponse>> getMenuByDate(@PathVariable String date) {
        return ResponseEntity.ok(getMenuByDateUseCase.execute(date));
    }

    @PutMapping("/menu/{id}")
    public ResponseEntity<DailyMenuItemResponse> updateDailyMenu(
            @PathVariable UUID id,
            @Valid @RequestBody DailyMenuRequest request) {
        return ResponseEntity.ok(updateDailyMenuUseCase.execute(id, request));
    }

    @DeleteMapping("/menu/{id}")
    public ResponseEntity<Void> deleteDailyMenu(@PathVariable UUID id) {
        deleteDailyMenuUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/menu/pdf/{date}")
    public ResponseEntity<byte[]> generateMenuPdf(@PathVariable String date) {
        byte[] pdfBytes = generateDailyMenuPdfUseCase.execute(date);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "menu_plan_" + date + ".pdf");
        
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
