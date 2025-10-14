package com.simox.pneumia_backend.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "diagnoses")
@Data
public class Diagnosis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diagnosis_id")
    private Long diagnosisId;
    
    @ManyToOne
    @JoinColumn(name = "image_id", nullable = false)
    private XrayImage image;
    
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    
    @Column(name = "ai_confidence_score")
    private BigDecimal aiConfidenceScore;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "ai_prediction", nullable = false)
    private Prediction aiPrediction;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "doctor_review_status")
    private ReviewStatus doctorReviewStatus;
    
    @Column(name = "doctor_notes")
    private String doctorNotes;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "final_diagnosis")
    private Prediction finalDiagnosis;
    
    @Column(name = "diagnosis_timestamp")
    private LocalDateTime diagnosisTimestamp;
}


