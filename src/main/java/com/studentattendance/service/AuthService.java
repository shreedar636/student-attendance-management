package com.studentattendance.service;

import com.studentattendance.dtos.LoginRequest;
import com.studentattendance.dtos.LoginResponse;
import com.studentattendance.dtos.RegisterRequest;
import com.studentattendance.dtos.RegisterResponse;
import com.studentattendance.entity.User;
import com.studentattendance.errorhandler.ServiceResult;
import com.studentattendance.jwt.JwtService;
import com.studentattendance.models.Role;
import com.studentattendance.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder,
                       UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public ServiceResult<RegisterResponse> register(RegisterRequest request) {
        try {
            if (userRepository.existsByEmail(request.getEmail())) {
                return ServiceResult.error(
                        new RegisterResponse(400, "Email already exists"),
                        "EMAIL_EXISTS"
                );
            }

            User user = new User();
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            user.setRoles(Set.of(Role.ADMIN));

            userRepository.save(user);

            return ServiceResult.success(
                    new RegisterResponse(200, "User registered successfully")
            );
        } catch (Exception e) {
            return ServiceResult.error(
                    new RegisterResponse(500, "Registration failed"),
                    "REGISTRATION_FAILED"
            );
        }
    }

    public ServiceResult<LoginResponse> login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String jwtToken = jwtService.generateToken(
                    org.springframework.security.core.userdetails.User
                            .withUsername(user.getEmail())
                            .password(user.getPassword())
                            .roles(user.getRoles().stream().map(Enum::name).toArray(String[]::new))
                            .build()
            );

            return ServiceResult.success(
                    new LoginResponse(200, "Login successful", jwtToken)
            );
        } catch (Exception e) {
            return ServiceResult.error(
                    new LoginResponse(401, "Invalid email or password", null),
                    "AUTH_FAILED"
            );
        }
    }
}
