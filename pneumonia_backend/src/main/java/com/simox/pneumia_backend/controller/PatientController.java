package com.simox.pneumia_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.simox.pneumia_backend.repository.DiagnosisRepository;
import com.simox.pneumia_backend.dto.UploadXrayRequest;
import com.simox.pneumia_backend.repository.PatientRepository;
import com.simox.pneumia_backend.service.PatientService;

import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import com.simox.pneumia_backend.service.FileStorageService;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;
    private final PatientRepository patientRepository;

    @PostMapping("/xrays")
    public ResponseEntity<?> upload(@RequestParam Long patientId, @Valid @RequestBody UploadXrayRequest req) {
        return ResponseEntity.ok(patientService.uploadXray(patientId, req));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestParam Long userId) {
        return patientRepository.findByUserUserId(userId)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());

    }
    private final FileStorageService storage;
    private final DiagnosisRepository diagnosisRepo;

    @PostMapping("/xrays/upload")
    public ResponseEntity<?> uploadFile(@RequestParam Long patientId,
                                        @RequestPart("file") MultipartFile file
                                         ) {
        String savedPath = storage.save(file);
        UploadXrayRequest req = new UploadXrayRequest();
        req.originalFilename = file.getOriginalFilename();
        req.filePath = savedPath;
        // reuse your existing flow (saves image, calls FastAPI, creates diagnosis)
        return ResponseEntity.ok(patientService.uploadXray(patientId, req));
    }
    @GetMapping("/diagnoses")
    public ResponseEntity<?> diagnoses(@RequestParam Long patientId) {
        return ResponseEntity.ok(diagnosisRepo.findByPatientPatientId(patientId));
    }

}
