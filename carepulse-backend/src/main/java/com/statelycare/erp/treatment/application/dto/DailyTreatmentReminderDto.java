package com.statelycare.erp.treatment.application.dto;

import com.statelycare.erp.treatment.domain.model.Frequency;
import com.statelycare.erp.treatment.domain.model.TreatmentStatus;
import com.statelycare.erp.treatment.domain.model.TreatmentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record DailyTreatmentReminderDto(
    UUID treatmentId,
    UUID residentId,
    String residentName,
    String residentCode,
    String treatmentName,
    TreatmentType treatmentType,
    Frequency frequency,
    LocalTime scheduledTime,
    boolean administeredToday,
    TreatmentStatus status
) {}