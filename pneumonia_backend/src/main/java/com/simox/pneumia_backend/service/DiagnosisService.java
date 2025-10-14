package com.simox.pneumia_backend.service;

import org.springframework.stereotype.Service;

import com.simox.pneumia_backend.entity.Diagnosis;
import com.simox.pneumia_backend.entity.Prediction;
import com.simox.pneumia_backend.entity.ReviewStatus;
import com.simox.pneumia_backend.entity.XrayImage;
import com.simox.pneumia_backend.repository.DiagnosisRepository;
import com.simox.pneumia_backend.repository.XrayImageRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiagnosisService {
    private final DiagnosisRepository diagnosisRepository;
    private final XrayImageRepository xrayRepo;
    @Transactional
    public Diagnosis createForImage(Long imageId, Prediction pred, java.math.BigDecimal conf) {
        XrayImage img = xrayRepo.findById(imageId).orElseThrow();
        Diagnosis d = new Diagnosis();
        d.setImage(img);
        d.setPatient(img.getPatient());
        d.setAiPrediction(pred);
        d.setAiConfidenceScore(conf);
        d.setDoctorReviewStatus(ReviewStatus.PENDING);
        d.setDiagnosisTimestamp(java.time.LocalDateTime.now());
        return diagnosisRepository.save(d);
    }


    public Diagnosis findById(Long diagnosisId) {
        return diagnosisRepository.findById(diagnosisId)
            .orElseThrow(() -> new RuntimeException("Diagnosis not found"));
    }
    
    public Diagnosis save(Diagnosis diagnosis) {
        return diagnosisRepository.save(diagnosis);
    }
}
