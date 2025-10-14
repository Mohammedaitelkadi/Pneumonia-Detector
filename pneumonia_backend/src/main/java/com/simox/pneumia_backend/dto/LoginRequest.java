package com.simox.pneumia_backend.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank public String usernameOrEmail;
    @NotBlank public String password;
}
