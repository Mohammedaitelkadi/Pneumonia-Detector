package com.simox.pneumia_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simox.pneumia_backend.dto.LoginRequest;
import com.simox.pneumia_backend.dto.RegisterRequest;
import com.simox.pneumia_backend.entity.User;
import com.simox.pneumia_backend.service.DoctorService;
import com.simox.pneumia_backend.service.PatientService;
import com.simox.pneumia_backend.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final PatientService patientService;
    private final DoctorService doctorService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        User u = userService.register(req);
        if (u.getUserType() == User.UserType.PATIENT) {
            patientService.createProfileFor(u);
        } else {
            doctorService.createProfileFor(u, "TEMP-LIC-" + u.getUserId());
        }
        return ResponseEntity.ok("Registered: " + u.getUsername());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        User u = userService.findByUsernameOrEmail(req.usernameOrEmail).orElseThrow(() -> new RuntimeException("Not found"));
        return ResponseEntity.ok("Logged in: " + u.getUsername());
    }
}
