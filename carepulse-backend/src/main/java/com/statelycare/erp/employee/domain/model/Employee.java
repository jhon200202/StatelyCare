package com.statelycare.erp.employee.domain.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record Employee(
    UUID id,
    UUID userId,
    String employeeCode,
    String firstName,
    String lastName,
    Department department,
    String roleTitle,
    LocalDate hireDate,
    CertificationStatus certificationStatus,
    String phone,
    String email,
    ShiftPreference shiftPreference,
    boolean isActive,
    Instant createdAt,
    Instant updatedAt
) {
    public static Employee createNew(
            UUID userId, 
            String employeeCode, 
            String firstName, 
            String lastName, 
            Department department, 
            String roleTitle, 
            LocalDate hireDate) {
        Instant now = Instant.now();
        return new Employee(
            UUID.randomUUID(), 
            userId, 
            employeeCode, 
            firstName, 
            lastName, 
            department, 
            roleTitle, 
            hireDate, 
            CertificationStatus.PENDING, 
            null, 
            null, 
            null, 
            true, 
            now, 
            now
        );
    }
    public Employee update(
            String firstName,
            String lastName,
            Department department,
            String roleTitle,
            LocalDate hireDate
    ) {
        return new Employee(
                this.id,
                this.userId,
                this.employeeCode,
                firstName,
                lastName,
                department,
                roleTitle,
                hireDate,
                this.certificationStatus,
                this.phone,
                this.email,
                this.shiftPreference,
                this.isActive,
                this.createdAt,
                Instant.now()
        );
    }
}
