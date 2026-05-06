package com.statelycare.erp.resident.infrastructure.web;

import com.statelycare.erp.resident.application.dto.ResidentRequest;
import com.statelycare.erp.resident.application.dto.ResidentResponse;
import com.statelycare.erp.resident.application.usecase.CreateResidentUseCase;
import com.statelycare.erp.resident.application.usecase.DeleteResidentUseCase;
import com.statelycare.erp.resident.application.usecase.GetResidentListUseCase;
import com.statelycare.erp.resident.application.usecase.UpdateResidentUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/residents")
public class ResidentController {

    private final GetResidentListUseCase getResidentListUseCase;
    private final CreateResidentUseCase createResidentUseCase;
    private final UpdateResidentUseCase updateResidentUseCase;
    private final DeleteResidentUseCase deleteResidentUseCase;

    public ResidentController(
            GetResidentListUseCase getResidentListUseCase,
            CreateResidentUseCase createResidentUseCase,
            UpdateResidentUseCase updateResidentUseCase,
            DeleteResidentUseCase deleteResidentUseCase) {
        this.getResidentListUseCase = getResidentListUseCase;
        this.createResidentUseCase = createResidentUseCase;
        this.updateResidentUseCase = updateResidentUseCase;
        this.deleteResidentUseCase = deleteResidentUseCase;
    }

    @GetMapping
    public ResponseEntity<List<ResidentResponse>> getAllResidents(
            @RequestParam(required = false) String query,
            @RequestParam(required = false, defaultValue = "firstName") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction) {
        return ResponseEntity.ok(getResidentListUseCase.execute(query, sortBy, direction));
    }

    @PostMapping
    public ResponseEntity<ResidentResponse> createResident(@Valid @RequestBody ResidentRequest request) {
        return new ResponseEntity<>(createResidentUseCase.execute(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResidentResponse> updateResident(
            @PathVariable UUID id,
            @Valid @RequestBody ResidentRequest request) {
        return ResponseEntity.ok(updateResidentUseCase.execute(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResident(@PathVariable UUID id) {
        deleteResidentUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}