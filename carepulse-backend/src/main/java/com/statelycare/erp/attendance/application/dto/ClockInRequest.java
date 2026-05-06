package com.statelycare.erp.attendance.application.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ClockInRequest(
    @NotNull(message = "Staff ID is required")
    UUID staffId
) {}
