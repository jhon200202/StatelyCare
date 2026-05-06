package com.statelycare.erp.treatment.application.dto;

import com.statelycare.erp.treatment.domain.model.MedicationRoute;
import java.util.UUID;

public record MedicationAdministrationRequest(
    UUID treatmentId,
    UUID administeredBy,
    String dosageGiven,
    MedicationRoute route,
    String notes
) {}
