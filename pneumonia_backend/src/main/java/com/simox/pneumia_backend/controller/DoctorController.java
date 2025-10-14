package com.simox.pneumia_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simox.pneumia_backend.entity.Diagnosis;
import com.simox.pneumia_backend.entity.Prediction;
import com.simox.pneumia_backend.entity.ReviewStatus;
import com.simox.pneumia_backend.service.DoctorService;
import com.simox.pneumia_backend.dto.ReviewDiagnosisRequest;
import com.simox.pneumia_backend.service.DiagnosisService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;
    private final DiagnosisService diagnosisService;

    @GetMapping("/reviews")
    public ResponseEntity<?> pending(@RequestParam Long doctorId) {
        return ResponseEntity.ok(doctorService.getPendingReviews(doctorId));
    }

    @PostMapping("/diagnoses/{diagnosisId}/review")
    public ResponseEntity<?> reviewDiagnosis(
    @PathVariable Long diagnosisId,
    @RequestBody ReviewDiagnosisRequest req) {
    
    Diagnosis diagnosis = diagnosisService.findById(diagnosisId);
    diagnosis.setDoctorReviewStatus(ReviewStatus.valueOf(req.status));
    diagnosis.setDoctorNotes(req.notes);
    diagnosis.setFinalDiagnosis(Prediction.valueOf(req.finalDiagnosis));
    
    diagnosisService.save(diagnosis);
    return ResponseEntity.ok("Review completed");
}
}
