package com.statelycare.erp.treatment.application.usecase;

import com.statelycare.erp.resident.domain.repository.ResidentRepository;
import com.statelycare.erp.treatment.application.dto.DailyTreatmentReminderDto;
import com.statelycare.erp.treatment.domain.model.Frequency;
import com.statelycare.erp.treatment.domain.model.TreatmentStatus;
import com.statelycare.erp.treatment.domain.repository.MedicationAdministrationRepository;
import com.statelycare.erp.treatment.domain.repository.TreatmentRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GetDailyTreatmentsUseCase {

    private final TreatmentRepository treatmentRepository;
    private final ResidentRepository residentRepository;
    private final MedicationAdministrationRepository administrationRepository;

    public GetDailyTreatmentsUseCase(
            TreatmentRepository treatmentRepository,
            ResidentRepository residentRepository,
            MedicationAdministrationRepository administrationRepository) {
        this.treatmentRepository = treatmentRepository;
        this.residentRepository = residentRepository;
        this.administrationRepository = administrationRepository;
    }

    public List<DailyTreatmentReminderDto> execute() {
        LocalDate today = LocalDate.now();
        
        var residents = residentRepository.findAll();
        var residentsMap = residents.stream()
                .collect(Collectors.toMap(r -> r.id(), r -> r));

        var allTreatments = residents.stream()
                .flatMap(r -> treatmentRepository.findActiveByResidentId(r.id()).stream())
                .filter(t -> t.frequency() == Frequency.DAILY)
                .filter(t -> t.status() == TreatmentStatus.ACTIVE)
                .filter(t -> !t.startDate().isAfter(today))
                .filter(t -> t.endDate() == null || !t.endDate().isBefore(today))
                .collect(Collectors.toList());

        var todayAdministrations = administrationRepository.findAll().stream()
                .filter(a -> a.actualTime() != null)
                .filter(a -> {
                    LocalDate adminDate = a.actualTime().atZone(ZoneId.systemDefault()).toLocalDate();
                    return adminDate.equals(today);
                })
                .collect(Collectors.toSet());

        return allTreatments.stream()
                .map(t -> {
                    var resident = residentsMap.get(t.residentId());
                    String residentName = resident != null ? 
                            resident.firstName() + " " + resident.lastName() : "Desconocido";
                    String residentCode = resident != null ? resident.residentCode() : "-";
                    
                    boolean administeredToday = todayAdministrations.stream()
                            .anyMatch(a -> a.treatmentId().equals(t.id()));

                    return new DailyTreatmentReminderDto(
                            t.id(),
                            t.residentId(),
                            residentName,
                            residentCode,
                            t.treatmentName(),
                            t.treatmentType(),
                            t.frequency(),
                            t.scheduledTime(),
                            administeredToday,
                            t.status()
                    );
                })
                .collect(Collectors.toList());
    }
}