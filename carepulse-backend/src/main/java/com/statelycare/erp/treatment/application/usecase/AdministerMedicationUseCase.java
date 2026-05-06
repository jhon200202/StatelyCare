package com.statelycare.erp.treatment.application.usecase;

import com.statelycare.erp.employee.domain.repository.EmployeeRepository;
import com.statelycare.erp.treatment.application.dto.MedicationAdministrationRequest;
import com.statelycare.erp.treatment.application.dto.MedicationAdministrationResponse;
import com.statelycare.erp.treatment.domain.model.AdministrationStatus;
import com.statelycare.erp.treatment.domain.model.MedicationAdministration;
import com.statelycare.erp.treatment.domain.model.MedicationRoute;
import com.statelycare.erp.treatment.domain.repository.MedicationAdministrationRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.UUID;

@Service
public class AdministerMedicationUseCase {

    private final MedicationAdministrationRepository repository;
    private final EmployeeRepository employeeRepository;

    public AdministerMedicationUseCase(MedicationAdministrationRepository repository, EmployeeRepository employeeRepository) {
        this.repository = repository;
        this.employeeRepository = employeeRepository;
    }

    public MedicationAdministrationResponse execute(MedicationAdministrationRequest request) {
        UUID administeredById = employeeRepository.findByUserId(request.administeredBy())
            .map(emp -> emp.id())
            .orElse(null);

        MedicationAdministration admin = new MedicationAdministration(
            UUID.randomUUID(),
            request.treatmentId(),
            administeredById,
            Instant.now(),
            Instant.now(),
            request.dosageGiven(),
            request.route(),
            AdministrationStatus.ADMINISTERED,
            request.notes(),
            null,
            null,
            Instant.now(),
            Instant.now()
        );

        MedicationAdministration saved = repository.save(admin);
        return mapToResponse(saved);
    }

    private MedicationAdministrationResponse mapToResponse(MedicationAdministration a) {
        return new MedicationAdministrationResponse(
            a.id(), a.treatmentId(), a.administeredBy(), a.scheduledTime(),
            a.actualTime(), a.dosageGiven(), a.route(), a.status(), a.notes()
        );
    }
}
