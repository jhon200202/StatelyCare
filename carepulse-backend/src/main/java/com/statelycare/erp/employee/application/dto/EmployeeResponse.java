package com.statelycare.erp.employee.application.dto;

import com.statelycare.erp.employee.domain.model.CertificationStatus;
import com.statelycare.erp.employee.domain.model.Department;
import java.time.LocalDate;
import java.util.UUID;

public record EmployeeResponse(
    UUID id,
    UUID userId,
    String employeeCode,
    String firstName,
    String lastName,
    Department department,
    String roleTitle,
    LocalDate hireDate,
    CertificationStatus certificationStatus,
    boolean isActive
) {}
