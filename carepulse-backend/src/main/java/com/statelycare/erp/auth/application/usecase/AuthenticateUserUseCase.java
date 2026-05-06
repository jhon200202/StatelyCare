package com.statelycare.erp.auth.application.usecase;

import com.statelycare.erp.auth.application.dto.LoginRequest;
import com.statelycare.erp.auth.application.dto.LoginResponse;
import com.statelycare.erp.auth.domain.model.User;
import com.statelycare.erp.auth.domain.repository.UserRepository;
import com.statelycare.erp.auth.infrastructure.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateUserUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticateUserUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse execute(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.passwordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        if (!user.isActive()) {
            throw new IllegalStateException("User account is disabled");
        }

        String token = jwtService.generateToken(user);
        
        return new LoginResponse(
            token,
            user.id(),
            user.username(),
            user.role()
        );
    }
}
