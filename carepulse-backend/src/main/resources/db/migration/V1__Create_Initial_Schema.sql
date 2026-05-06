-- 1. Usuarios (Autenticación)
CREATE TABLE IF NOT EXISTS users (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  username VARCHAR(255) NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  role VARCHAR(50) NOT NULL,
  is_active TINYINT(1) NOT NULL DEFAULT 1,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  last_login TIMESTAMP NULL,
  deleted_at TIMESTAMP NULL,
  created_by VARCHAR(36) NULL,
  updated_by VARCHAR(36) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Habitaciones
CREATE TABLE IF NOT EXISTS rooms (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  room_number VARCHAR(50) NOT NULL UNIQUE,
  wing VARCHAR(50) NOT NULL,
  floor INT NOT NULL,
  capacity INT NOT NULL,
  current_occupancy INT NOT NULL DEFAULT 0,
  room_type VARCHAR(50) NOT NULL,
  is_active TINYINT(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Residentes
CREATE TABLE IF NOT EXISTS residents (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  resident_code VARCHAR(50) NOT NULL UNIQUE,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  date_of_birth DATE NOT NULL,
  gender VARCHAR(20) NOT NULL,
  room_id VARCHAR(36) NULL,
  admission_date DATE NOT NULL,
  primary_physician_id VARCHAR(36) NULL,
  care_plan LONGTEXT NULL,
  medical_history_summary LONGTEXT NULL,
  status VARCHAR(50) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP NULL,
  CONSTRAINT fk_residents_room FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Empleados
CREATE TABLE IF NOT EXISTS employees (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  user_id VARCHAR(36) NULL,
  employee_code VARCHAR(50) NOT NULL UNIQUE,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  department VARCHAR(100) NOT NULL,
  role_title VARCHAR(100) NOT NULL,
  hire_date DATE NOT NULL,
  certification_status VARCHAR(50) NOT NULL,
  phone VARCHAR(50) NULL,
  email VARCHAR(255) NULL,
  shift_preference VARCHAR(50) NULL,
  is_active TINYINT(1) NOT NULL DEFAULT 1,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP NULL,
  CONSTRAINT fk_employees_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Registro de Asistencia
CREATE TABLE IF NOT EXISTS attendance_records (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  employee_id VARCHAR(36) NOT NULL,
  shift_date DATE NOT NULL,
  scheduled_start TIME NOT NULL,
  scheduled_end TIME NOT NULL,
  actual_clock_in TIMESTAMP NULL,
  actual_clock_out TIMESTAMP NULL,
  status VARCHAR(50) NOT NULL,
  late_minutes INT NOT NULL DEFAULT 0,
  total_hours DOUBLE NULL,
  notes LONGTEXT NULL,
  approved_by VARCHAR(36) NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_attendance_employee FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Tratamientos Médicos
CREATE TABLE IF NOT EXISTS treatments (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  resident_id VARCHAR(36) NOT NULL,
  prescribed_by VARCHAR(36) NULL,
  treatment_name VARCHAR(255) NOT NULL,
  treatment_type VARCHAR(50) NOT NULL,
  description LONGTEXT NULL,
  frequency VARCHAR(50) NOT NULL,
  scheduled_time TIME NULL,
  start_date DATE NOT NULL,
  end_date DATE NULL,
  status VARCHAR(50) NOT NULL,
  instructions LONGTEXT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP NULL,
  CONSTRAINT fk_treatments_resident FOREIGN KEY (resident_id) REFERENCES residents(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. Administración de Medicamentos
CREATE TABLE IF NOT EXISTS medication_administrations (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  treatment_id VARCHAR(36) NOT NULL,
  administered_by VARCHAR(36) NULL,
  scheduled_time TIMESTAMP NOT NULL,
  actual_time TIMESTAMP NULL,
  dosage_given VARCHAR(100) NULL,
  route VARCHAR(50) NOT NULL,
  status VARCHAR(50) NOT NULL,
  notes LONGTEXT NULL,
  pain_level_before INT NULL,
  pain_level_after INT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_administrations_treatment FOREIGN KEY (treatment_id) REFERENCES treatments(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 8. Transacciones Financieras
CREATE TABLE IF NOT EXISTS financial_transactions (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  transaction_code VARCHAR(50) NOT NULL UNIQUE,
  transaction_type VARCHAR(50) NOT NULL,
  category VARCHAR(100) NOT NULL,
  amount DECIMAL(19, 4) NOT NULL,
  currency VARCHAR(10) NOT NULL DEFAULT 'USD',
  description LONGTEXT NULL,
  resident_id VARCHAR(36) NULL,
  vendor_name VARCHAR(255) NULL,
  invoice_number VARCHAR(100) NULL,
  payment_method VARCHAR(50) NOT NULL,
  status VARCHAR(50) NOT NULL,
  transaction_date DATE NOT NULL,
  due_date DATE NULL,
  settled_date DATE NULL,
  created_by VARCHAR(36) NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP NULL,
  CONSTRAINT fk_transactions_resident FOREIGN KEY (resident_id) REFERENCES residents(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 9. Nutrición - Catálogo de Alimentos
CREATE TABLE IF NOT EXISTS menu_items (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description LONGTEXT NULL,
  meal_type VARCHAR(50) NOT NULL,
  texture_modification VARCHAR(50) NOT NULL,
  is_active TINYINT(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 10. Nutrición - Menú Diario
CREATE TABLE IF NOT EXISTS daily_menus (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  menu_date DATE NOT NULL,
  menu_item_id VARCHAR(36) NOT NULL,
  meal_type VARCHAR(50) NOT NULL,
  servings_planned INT NOT NULL,
  servings_actual INT NULL,
  notes LONGTEXT NULL,
  CONSTRAINT fk_daily_menus_item FOREIGN KEY (menu_item_id) REFERENCES menu_items(id) ON DELETE CASCADE,
  INDEX idx_daily_menus_date (menu_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
