package com.statelycare.erp.employee.application.usecase;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.statelycare.erp.employee.domain.repository.EmployeeRepository;

@Service
public class DeleteEmployeeUseCase {
    private final EmployeeRepository employeeRepository;

    public DeleteEmployeeUseCase(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void execute(String id) {
        UUID uuid = UUID.fromString(id);
        employeeRepository.deleteById(uuid);
    }
}
