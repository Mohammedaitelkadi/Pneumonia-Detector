package com.simox.pneumia_backend.repository;

import com.simox.pneumia_backend.entity.XrayImage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface XrayImageRepository extends JpaRepository<XrayImage, Long> {
    List<XrayImage> findByPatientPatientIdOrderByUploadTimestampDesc(Long patientId);
}
