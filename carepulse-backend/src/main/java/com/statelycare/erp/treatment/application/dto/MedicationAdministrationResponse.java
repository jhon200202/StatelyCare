package com.statelycare.erp.treatment.application.dto;

import com.statelycare.erp.treatment.domain.model.AdministrationStatus;
import com.statelycare.erp.treatment.domain.model.MedicationRoute;
import java.time.Instant;
import java.util.UUID;

public record MedicationAdministrationResponse(
    UUID id,
    UUID treatmentId,
    UUID administeredBy,
    Instant scheduledTime,
    Instant actualTime,
    String dosageGiven,
    MedicationRoute route,
    AdministrationStatus status,
    String notes
) {}
