-- 创建医生用户表
CREATE TABLE IF NOT EXISTS doctor_user (
    id VARCHAR(20) NOT NULL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(50) NOT NULL,
    title VARCHAR(30),
    department VARCHAR(30),
    avatar TEXT,
    experience VARCHAR(50),
    is_active BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建医生专长表
CREATE TABLE IF NOT EXISTS doctor_specialties (
    doctor_id VARCHAR(20) NOT NULL,
    specialty VARCHAR(100) NOT NULL,
    PRIMARY KEY (doctor_id, specialty),
    FOREIGN KEY (doctor_id) REFERENCES doctor_user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
