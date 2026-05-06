package com.statelycare.erp.auth.infrastructure.web;

import com.statelycare.erp.auth.application.dto.UserRequest;
import com.statelycare.erp.auth.application.dto.UserResponse;
import com.statelycare.erp.auth.domain.model.User;
import com.statelycare.erp.auth.domain.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userRepository.findAll().stream()
            .map(UserResponse::fromDomain)
            .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        return userRepository.findById(id)
            .map(user -> ResponseEntity.ok(UserResponse.fromDomain(user)))
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            return ResponseEntity.badRequest().build();
        }
        if (userRepository.existsByUsername(request.username())) {
            return ResponseEntity.badRequest().build();
        }

        String hashedPassword = passwordEncoder.encode(request.passwordHash());
        
        User newUser = User.createNew(
            request.username(),
            request.email(),
            hashedPassword,
            request.role()
        );
        
        User savedUser = userRepository.save(newUser);
        return new ResponseEntity<>(UserResponse.fromDomain(savedUser), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID id, @Valid @RequestBody UserRequest request) {
        return userRepository.findById(id)
            .map(existingUser -> {
                Instant now = Instant.now();
                
                User updatedUser = new User(
                    existingUser.id(),
                    request.username() != null ? request.username() : existingUser.username(),
                    request.email() != null ? request.email() : existingUser.email(),
                    request.passwordHash() != null && !request.passwordHash().isEmpty() 
                        ? passwordEncoder.encode(request.passwordHash()) 
                        : existingUser.passwordHash(),
                    request.role() != null ? request.role() : existingUser.role(),
                    request.isActive() != null ? request.isActive() : existingUser.isActive(),
                    existingUser.createdAt(),
                    now,
                    existingUser.lastLogin()
                );
                
                User savedUser = userRepository.save(updatedUser);
                return ResponseEntity.ok(UserResponse.fromDomain(savedUser));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userRepository.delete(id);
        return ResponseEntity.noContent().build();
    }
}