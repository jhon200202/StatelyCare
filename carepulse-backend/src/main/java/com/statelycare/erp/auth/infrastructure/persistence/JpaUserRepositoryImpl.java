package com.statelycare.erp.auth.infrastructure.persistence;

import com.statelycare.erp.auth.domain.model.User;
import com.statelycare.erp.auth.domain.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JpaUserRepositoryImpl implements UserRepository {

    private final SpringDataUserRepository repository;

    public JpaUserRepositoryImpl(SpringDataUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> findAll() {
        return repository.findByDeletedAtIsNull().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(UUID id) {
        return repository.findByIdAndDeletedAtIsNull(id).map(this::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email)
            .filter(e -> e.getDeletedAt() == null)
            .map(this::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username)
            .filter(e -> e.getDeletedAt() == null)
            .map(this::toDomain);
    }

    @Override
    public User save(User user) {
        UserEntity entity = toEntity(user);
        return toDomain(repository.save(entity));
    }

    @Override
    public void delete(UUID id) {
        repository.findById(id).ifPresent(entity -> {
            entity.setDeletedAt(Instant.now());
            repository.save(entity);
        });
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    private User toDomain(UserEntity entity) {
        return new User(
            entity.getId(),
            entity.getUsername(),
            entity.getEmail(),
            entity.getPasswordHash(),
            entity.getRole(),
            entity.isActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.getLastLogin()
        );
    }

    private UserEntity toEntity(User domain) {
        return new UserEntity(
            domain.id(),
            domain.username(),
            domain.email(),
            domain.passwordHash(),
            domain.role(),
            domain.isActive(),
            domain.createdAt(),
            domain.updatedAt()
        );
    }
}
