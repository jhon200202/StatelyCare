package com.statelycare.erp.auth.application.dto;

import java.util.UUID;
import com.statelycare.erp.auth.domain.model.Role;

public record LoginResponse(
    String token,
    UUID userId,
    String username,
    Role role
) {}
