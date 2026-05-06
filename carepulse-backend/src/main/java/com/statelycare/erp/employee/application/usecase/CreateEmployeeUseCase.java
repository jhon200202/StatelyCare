package com.statelycare.erp.employee.application.usecase;

import com.statelycare.erp.employee.application.dto.EmployeeRequest;
import com.statelycare.erp.employee.application.dto.EmployeeResponse;
import com.statelycare.erp.employee.domain.model.Employee;
import com.statelycare.erp.employee.domain.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class CreateEmployeeUseCase {

    private final EmployeeRepository repository;

    public CreateEmployeeUseCase(EmployeeRepository repository) {
        this.repository = repository;
    }

    public EmployeeResponse execute(EmployeeRequest request) {
        String employeeCode = "EMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        Employee employee = Employee.createNew(
            request.userId(),
            employeeCode,
            request.firstName(),
            request.lastName(),
            request.department(),
            request.roleTitle(),
            request.hireDate()
        );

        Employee saved = repository.save(employee);
        return mapToResponse(saved);
    }

    private EmployeeResponse mapToResponse(Employee e) {
        return new EmployeeResponse(
            e.id(),
            e.userId(),
            e.employeeCode(),
            e.firstName(),
            e.lastName(),
            e.department(),
            e.roleTitle(),
            e.hireDate(),
            e.certificationStatus(),
            e.isActive()
        );
    }
}
