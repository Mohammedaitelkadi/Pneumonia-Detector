package com.simox.pneumia_backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.simox.pneumia_backend.dto.AiAnalysisResponse;
import com.simox.pneumia_backend.dto.UploadXrayRequest;
import com.simox.pneumia_backend.entity.Gender;
import com.simox.pneumia_backend.entity.Patient;
import com.simox.pneumia_backend.entity.Prediction;
import com.simox.pneumia_backend.entity.User;
import com.simox.pneumia_backend.entity.XrayImage;
import com.simox.pneumia_backend.repository.PatientRepository;
import com.simox.pneumia_backend.repository.UserRepository;
import com.simox.pneumia_backend.repository.XrayImageRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final XrayImageRepository xrayRepo;
    private final AiAnalysisClient aiAnalysisClient; // Add this
    private final DiagnosisService diagnosisService;

    public Patient createProfileFor(User user) {
        Patient p = new Patient();
        p.setUser(user);
        p.setFirstName(user.getUsername());
        p.setLastName("Patient");
        p.setDateOfBirth(java.time.LocalDate.now().minusYears(30));
        p.setGender(Gender.OTHER);
        p.setCreatedAt(java.time.LocalDateTime.now());
        return patientRepository.save(p);
    }
    

    @Transactional
    public XrayImage uploadXray(Long patientId, UploadXrayRequest req) {
        Patient p = patientRepository.findById(patientId).orElseThrow();
        XrayImage img = new XrayImage();
        img.setPatient(p);
        img.setOriginalFilename(req.originalFilename);
        img.setFilePath(req.filePath);
        img.setUploadTimestamp(LocalDateTime.now());
        
        XrayImage savedImg = xrayRepo.save(img);

        try {
            AiAnalysisResponse aiResult = aiAnalysisClient.analyzeImage(savedImg.getFilePath());
            
            // Create diagnosis record
            diagnosisService.createForImage(
                savedImg.getImageId(),
                Prediction.valueOf(aiResult.ai_prediction),
                BigDecimal.valueOf(aiResult.ai_confidence)
            );
        } catch (Exception e) {
            // Log error but don't fail the upload
            System.err.println("AI analysis failed: " + e.getMessage());
        }
        
        return savedImg;
    }

}
