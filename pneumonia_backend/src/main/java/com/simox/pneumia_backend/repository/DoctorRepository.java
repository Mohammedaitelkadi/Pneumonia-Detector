package com.simox.pneumia_backend.repository;

import com.simox.pneumia_backend.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUserUserId(Long userId);
    Optional<Doctor> findByLicenseNumber(String licenseNumber);
}
