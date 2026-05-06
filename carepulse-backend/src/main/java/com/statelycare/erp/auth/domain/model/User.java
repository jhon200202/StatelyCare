package com.statelycare.erp.auth.domain.model;

import java.time.Instant;
import java.util.UUID;

public record User(
    UUID id,
    String username,
    String email,
    String passwordHash,
    Role role,
    boolean isActive,
    Instant createdAt,
    Instant updatedAt,
    Instant lastLogin
) {
    public User {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("Password hash cannot be empty");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
    }

    public static User createNew(String username, String email, String passwordHash, Role role) {
        Instant now = Instant.now();
        return new User(UUID.randomUUID(), username, email, passwordHash, role, true, now, now, null);
    }

    public User withLastLogin(Instant lastLogin) {
        return new User(id, username, email, passwordHash, role, isActive, createdAt, updatedAt, lastLogin);
    }

    public User withUpdatedAt(Instant updatedAt) {
        return new User(id, username, email, passwordHash, role, isActive, createdAt, updatedAt, lastLogin);
    }
}
