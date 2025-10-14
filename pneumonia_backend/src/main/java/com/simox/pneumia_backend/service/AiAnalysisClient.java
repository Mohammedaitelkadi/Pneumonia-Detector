package com.simox.pneumia_backend.service;

import com.simox.pneumia_backend.dto.AiAnalysisRequest;
import com.simox.pneumia_backend.dto.AiAnalysisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
public class AiAnalysisClient {
    private final WebClient fastApiWebClient;

    public AiAnalysisResponse analyzeImage(String filePath) {
        try {
            return fastApiWebClient.post()
                    .uri("/analyze")
                    .bodyValue(new AiAnalysisRequest(filePath))
                    .retrieve()
                    .bodyToMono(AiAnalysisResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("AI analysis failed: " + e.getMessage());
        }
    }
}
