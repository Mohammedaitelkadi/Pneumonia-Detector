package com.simox.pneumia_backend.dto;

public class AiAnalysisRequest {
    public String file_path;
    
    public AiAnalysisRequest(String filePath) {
        this.file_path = filePath;
    }
}
