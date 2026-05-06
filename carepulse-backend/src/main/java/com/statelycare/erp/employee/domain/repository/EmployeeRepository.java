package com.statelycare.erp.employee.domain.repository;

import com.statelycare.erp.employee.domain.model.Employee;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository {
    List<Employee> findAll();
    List<Employee> findAll(String query, String sortBy, String direction);
    Optional<Employee> findById(UUID id);
    Optional<Employee> findByUserId(UUID userId);
    Employee save(Employee employee);
    void deleteById(UUID id);
    long count();
}
