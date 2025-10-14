CREATE TABLE users (
    user_id        BIGSERIAL PRIMARY KEY,
    username       VARCHAR(50)  NOT NULL UNIQUE,
    email          VARCHAR(100) NOT NULL UNIQUE,
    password_hash  VARCHAR(255) NOT NULL,
    user_type      VARCHAR(20)  NOT NULL CHECK (user_type IN ('PATIENT','DOCTOR')),
    created_at     TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    is_active      BOOLEAN      DEFAULT TRUE
);

CREATE TABLE patients (
    patient_id   BIGSERIAL PRIMARY KEY,
    user_id      BIGINT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    first_name   VARCHAR(50)  NOT NULL,
    last_name    VARCHAR(50)  NOT NULL,
    date_of_birth DATE        NOT NULL,
    gender       VARCHAR(10)  NOT NULL CHECK (gender IN ('MALE','FEMALE','OTHER')),
    phone_number VARCHAR(20),
    created_at   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE doctors (
    doctor_id     BIGSERIAL PRIMARY KEY,
    user_id       BIGINT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    first_name    VARCHAR(50)  NOT NULL,
    last_name     VARCHAR(50)  NOT NULL,
    license_number VARCHAR(50) NOT NULL UNIQUE,
    specialization VARCHAR(100),
    created_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

-- X-ray images
CREATE TABLE xray_images (
    image_id          BIGSERIAL PRIMARY KEY,
    patient_id        BIGINT NOT NULL REFERENCES patients(patient_id) ON DELETE CASCADE,
    original_filename VARCHAR(255) NOT NULL,
    file_path         VARCHAR(500) NOT NULL,
    upload_timestamp  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE diagnoses (
    diagnosis_id         BIGSERIAL PRIMARY KEY,
    image_id             BIGINT NOT NULL REFERENCES xray_images(image_id) ON DELETE CASCADE,
    patient_id           BIGINT NOT NULL REFERENCES patients(patient_id) ON DELETE CASCADE,
    doctor_id            BIGINT REFERENCES doctors(doctor_id),
    ai_confidence_score  DECIMAL(5,4),
    ai_prediction        VARCHAR(20) NOT NULL CHECK (ai_prediction IN ('NORMAL','PNEUMONIA','UNCERTAIN')),
    doctor_review_status VARCHAR(20) DEFAULT 'PENDING' CHECK (doctor_review_status IN ('PENDING','REVIEWED','CONFIRMED','DISPUTED')),
    doctor_notes         TEXT,
    final_diagnosis      VARCHAR(20) CHECK (final_diagnosis IN ('NORMAL','PNEUMONIA','UNCERTAIN')),
    diagnosis_timestamp  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_type ON users(user_type);

-- Patients
CREATE INDEX idx_patients_user_id ON patients(user_id);
CREATE INDEX idx_patients_name ON patients(first_name, last_name);

-- Doctors
CREATE INDEX idx_doctors_user_id ON doctors(user_id);
CREATE INDEX idx_doctors_license ON doctors(license_number);

-- X-ray images
CREATE INDEX idx_xray_patient_id ON xray_images(patient_id);
CREATE INDEX idx_xray_upload_time ON xray_images(upload_timestamp);

-- Diagnoses
CREATE INDEX idx_diagnoses_patient_id ON diagnoses(patient_id);
CREATE INDEX idx_diagnoses_doctor_id ON diagnoses(doctor_id);
CREATE INDEX idx_diagnoses_status ON diagnoses(doctor_review_status);
CREATE INDEX idx_diagnoses_timestamp ON diagnoses(diagnosis_timestamp);