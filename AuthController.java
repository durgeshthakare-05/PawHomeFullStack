package com.pawhome.controller;

import com.pawhome.controller.Dto.*;
import com.pawhome.entity.User;
import com.pawhome.repository.UserRepository;
import com.pawhome.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    public AuthController(UserRepository users, PasswordEncoder encoder, JwtUtil jwt) {
        this.users = users; this.encoder = encoder; this.jwt = jwt;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        String email = req.email().trim().toLowerCase();
        if (users.existsByEmail(email))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
        User u = new User();
        u.setName(req.name().trim()); u.setEmail(email);
        u.setPasswordHash(encoder.encode(req.password()));
        u.setRole("ADOPTER"); u.setPhone(req.phone()); u.setCity(req.city());
        users.save(u);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse(jwt.generate(email, u.getRole()), u.getName(), email, u.getRole()));
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        User u = users.findByEmail(req.email().trim().toLowerCase())
                .filter(x -> encoder.matches(req.password(), x.getPasswordHash()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong email or password"));
        return new AuthResponse(jwt.generate(u.getEmail(), u.getRole()), u.getName(), u.getEmail(), u.getRole());
    }
}
