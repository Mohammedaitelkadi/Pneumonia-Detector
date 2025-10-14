package com.simox.pneumia_backend.repository;

import com.simox.pneumia_backend.entity.Diagnosis;
import com.simox.pneumia_backend.entity.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {
    List<Diagnosis> findByPatientPatientId(Long patientId);
    List<Diagnosis> findByDoctorDoctorIdAndDoctorReviewStatus(Long doctorId, ReviewStatus status);
}
