package com.statelycare.erp.employee.application.usecase;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.statelycare.erp.employee.application.dto.EmployeeRequest;
import com.statelycare.erp.employee.application.dto.EmployeeResponse;
import com.statelycare.erp.employee.domain.model.Employee;
import com.statelycare.erp.employee.domain.repository.EmployeeRepository;

@Service
public class UpdateEmployeeUseCase {
    private final EmployeeRepository employeeRepository;

    public UpdateEmployeeUseCase(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public EmployeeResponse execute(String id, EmployeeRequest request) {
        UUID uuid = UUID.fromString(id);
        Employee employee = employeeRepository.findById(uuid)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        // Actualiza los campos editables usando el método update del modelo
        employee = employee.update(
            request.firstName(),
            request.lastName(),
            request.department(),
            request.roleTitle(),
            request.hireDate()
        );
        Employee updated = employeeRepository.save(employee);
        return new EmployeeResponse(
                updated.id(),
                updated.userId(),
                updated.employeeCode(),
                updated.firstName(),
                updated.lastName(),
                updated.department(),
                updated.roleTitle(),
                updated.hireDate(),
                updated.certificationStatus(),
                updated.isActive()
        );
    }
}
