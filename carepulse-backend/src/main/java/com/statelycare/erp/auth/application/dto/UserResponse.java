package com.statelycare.erp.auth.application.dto;

import com.statelycare.erp.auth.domain.model.Role;
import com.statelycare.erp.auth.domain.model.User;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
    UUID id,
    String username,
    String email,
    Role role,
    boolean isActive,
    Instant lastLogin,
    Instant createdAt,
    Instant updatedAt
) {
    public static UserResponse fromDomain(User user) {
        return new UserResponse(
            user.id(),
            user.username(),
            user.email(),
            user.role(),
            user.isActive(),
            user.lastLogin(),
            user.createdAt(),
            user.updatedAt()
        );
    }
}