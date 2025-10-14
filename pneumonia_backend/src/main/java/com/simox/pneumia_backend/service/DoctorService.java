package com.simox.pneumia_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.simox.pneumia_backend.entity.Diagnosis;
import com.simox.pneumia_backend.entity.Doctor;
import com.simox.pneumia_backend.entity.ReviewStatus;
import com.simox.pneumia_backend.entity.User;
import com.simox.pneumia_backend.repository.DiagnosisRepository;
import com.simox.pneumia_backend.repository.DoctorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final DiagnosisRepository diagnosisRepository;

    public Doctor createProfileFor(User user, String licenseNumber) {
        Doctor d = new Doctor();
        d.setUser(user);
        d.setFirstName(user.getUsername());
        d.setLastName("Doctor");
        d.setLicenseNumber(licenseNumber);
        d.setCreatedAt(java.time.LocalDateTime.now());
        return doctorRepository.save(d);
    }

    public List<Diagnosis> getPendingReviews(Long doctorId) {
        return diagnosisRepository.findByDoctorDoctorIdAndDoctorReviewStatus(doctorId, ReviewStatus.PENDING);
    }
}
