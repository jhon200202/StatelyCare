package com.statelycare.erp.treatment.application.dto;

import com.statelycare.erp.treatment.domain.model.Frequency;
import com.statelycare.erp.treatment.domain.model.TreatmentType;
import java.time.LocalDate;
import java.util.UUID;

public record TreatmentRequest(
    UUID residentId,
    UUID prescribedBy,
    String treatmentName,
    TreatmentType treatmentType,
    Frequency frequency,
    LocalDate startDate
) {}
