package com.statelycare.erp.auth.application.dto;

import com.statelycare.erp.auth.domain.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequest(
    @NotBlank(message = "Username is required")
    @Size(min = 3, message = "Username must have at least 3 characters")
    String username,

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email format")
    String email,

    String passwordHash,

    @NotNull(message = "Role is required")
    Role role,

    Boolean isActive
) {}