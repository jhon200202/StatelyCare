package com.statelycare.erp.auth.domain.repository;

import com.statelycare.erp.auth.domain.model.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    List<User> findAll();
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    User save(User user);
    void delete(UUID id);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
