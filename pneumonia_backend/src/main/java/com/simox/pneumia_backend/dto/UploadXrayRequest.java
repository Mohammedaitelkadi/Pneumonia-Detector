package com.simox.pneumia_backend.dto;

public class UploadXrayRequest {
    @jakarta.validation.constraints.NotBlank public String originalFilename;
    @jakarta.validation.constraints.NotBlank public String filePath;
}
