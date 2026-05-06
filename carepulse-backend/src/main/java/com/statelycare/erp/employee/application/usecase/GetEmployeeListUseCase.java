package com.statelycare.erp.employee.application.usecase;

import com.statelycare.erp.employee.application.dto.EmployeeResponse;
import com.statelycare.erp.employee.domain.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetEmployeeListUseCase {

    private final EmployeeRepository repository;

    public GetEmployeeListUseCase(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<EmployeeResponse> execute(String query, String sortBy, String direction) {
        String finalSortBy = (sortBy == null || sortBy.isEmpty()) ? "firstName" : sortBy;
        String finalDirection = (direction == null || direction.isEmpty()) ? "asc" : direction;

        return repository.findAll(query, finalSortBy, finalDirection).stream()
                .map(e -> new EmployeeResponse(
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
                ))
                .collect(Collectors.toList());
    }

    public List<EmployeeResponse> execute() {
        return execute(null, "firstName", "asc");
    }
}
