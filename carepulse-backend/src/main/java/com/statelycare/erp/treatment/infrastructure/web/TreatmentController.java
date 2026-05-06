package com.statelycare.erp.treatment.infrastructure.web;

import com.statelycare.erp.treatment.application.dto.DailyTreatmentReminderDto;
import com.statelycare.erp.treatment.application.dto.MedicationAdministrationRequest;
import com.statelycare.erp.treatment.application.dto.MedicationAdministrationResponse;
import com.statelycare.erp.treatment.application.dto.TreatmentRequest;
import com.statelycare.erp.treatment.application.dto.TreatmentResponse;
import com.statelycare.erp.treatment.application.usecase.AdministerMedicationUseCase;
import com.statelycare.erp.treatment.application.usecase.CreateTreatmentUseCase;
import com.statelycare.erp.treatment.application.usecase.DeleteTreatmentUseCase;
import com.statelycare.erp.treatment.application.usecase.GetActiveTreatmentsByResidentUseCase;
import com.statelycare.erp.treatment.application.usecase.GetDailyTreatmentsUseCase;
import com.statelycare.erp.treatment.application.usecase.UpdateTreatmentUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/treatments")
public class TreatmentController {

    private final CreateTreatmentUseCase createTreatmentUseCase;
    private final GetActiveTreatmentsByResidentUseCase getActiveTreatmentsByResidentUseCase;
    private final AdministerMedicationUseCase administerMedicationUseCase;
    private final GetDailyTreatmentsUseCase getDailyTreatmentsUseCase;
    private final DeleteTreatmentUseCase deleteTreatmentUseCase;
    private final UpdateTreatmentUseCase updateTreatmentUseCase;

    public TreatmentController(CreateTreatmentUseCase createTreatmentUseCase,
                               GetActiveTreatmentsByResidentUseCase getActiveTreatmentsByResidentUseCase,
                               AdministerMedicationUseCase administerMedicationUseCase,
                               GetDailyTreatmentsUseCase getDailyTreatmentsUseCase,
                               DeleteTreatmentUseCase deleteTreatmentUseCase,
                               UpdateTreatmentUseCase updateTreatmentUseCase) {
        this.createTreatmentUseCase = createTreatmentUseCase;
        this.getActiveTreatmentsByResidentUseCase = getActiveTreatmentsByResidentUseCase;
        this.administerMedicationUseCase = administerMedicationUseCase;
        this.getDailyTreatmentsUseCase = getDailyTreatmentsUseCase;
        this.deleteTreatmentUseCase = deleteTreatmentUseCase;
        this.updateTreatmentUseCase = updateTreatmentUseCase;
    }

    @PostMapping
    public ResponseEntity<TreatmentResponse> createTreatment(@Valid @RequestBody TreatmentRequest request) {
        return new ResponseEntity<>(createTreatmentUseCase.execute(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TreatmentResponse> updateTreatment(@PathVariable UUID id, @Valid @RequestBody TreatmentRequest request) {
        return ResponseEntity.ok(updateTreatmentUseCase.execute(id, request));
    }

    @GetMapping("/resident/{residentId}")
    public ResponseEntity<List<TreatmentResponse>> getTreatmentsByResident(@PathVariable UUID residentId) {
        return ResponseEntity.ok(getActiveTreatmentsByResidentUseCase.execute(residentId));
    }

    @GetMapping("/daily")
    public ResponseEntity<List<DailyTreatmentReminderDto>> getDailyTreatments() {
        return ResponseEntity.ok(getDailyTreatmentsUseCase.execute());
    }

    @PostMapping("/administer")
    public ResponseEntity<MedicationAdministrationResponse> administerMedication(@Valid @RequestBody MedicationAdministrationRequest request) {
        return ResponseEntity.ok(administerMedicationUseCase.execute(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTreatment(@PathVariable UUID id) {
        deleteTreatmentUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
