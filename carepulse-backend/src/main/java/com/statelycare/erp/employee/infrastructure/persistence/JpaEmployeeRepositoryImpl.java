package com.statelycare.erp.employee.infrastructure.persistence;

import com.statelycare.erp.employee.domain.model.Employee;
import com.statelycare.erp.employee.domain.repository.EmployeeRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JpaEmployeeRepositoryImpl implements EmployeeRepository {

    private final SpringDataEmployeeRepository repository;

    public JpaEmployeeRepositoryImpl(SpringDataEmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Employee> findAll() {
        return repository.findByDeletedAtIsNull().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Employee> findAll(String query, String sortBy, String direction) {
        Sort sort = Sort.by(direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        return repository.searchEmployees(query, sort).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Employee> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Employee> findByUserId(UUID userId) {
        return repository.findByUserId(userId).map(this::toDomain);
    }

    @Override
    public Employee save(Employee employee) {
        return toDomain(repository.save(toEntity(employee)));
    }

    @Override
    public void deleteById(UUID id) {
        repository.findById(id).ifPresent(entity -> {
            entity.setDeletedAt(Instant.now());
            repository.save(entity);
        });
    }

    @Override
    public long count() {
        return repository.findByDeletedAtIsNull().size();
    }

    private Employee toDomain(EmployeeEntity entity) {
        return new Employee(
            entity.getId(),
            entity.getUserId(),
            entity.getEmployeeCode(),
            entity.getFirstName(),
            entity.getLastName(),
            entity.getDepartment(),
            entity.getRoleTitle(),
            entity.getHireDate(),
            entity.getCertificationStatus(),
            entity.getPhone(),
            entity.getEmail(),
            entity.getShiftPreference(),
            entity.isActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    private EmployeeEntity toEntity(Employee domain) {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setId(domain.id());
        entity.setUserId(domain.userId());
        entity.setEmployeeCode(domain.employeeCode());
        entity.setFirstName(domain.firstName());
        entity.setLastName(domain.lastName());
        entity.setDepartment(domain.department());
        entity.setRoleTitle(domain.roleTitle());
        entity.setHireDate(domain.hireDate());
        entity.setCertificationStatus(domain.certificationStatus());
        entity.setPhone(domain.phone());
        entity.setEmail(domain.email());
        entity.setShiftPreference(domain.shiftPreference());
        entity.setActive(domain.isActive());
        entity.setCreatedAt(domain.createdAt());
        entity.setUpdatedAt(domain.updatedAt());
        return entity;
    }
}
