package com.simox.pneumia_backend.dto;

import jakarta.validation.constraints.NotBlank;

public class ReviewDiagnosisRequest {
    @NotBlank public String status;
    public String notes;
    @NotBlank public String finalDiagnosis;
}
