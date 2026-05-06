package com.statelycare.erp.staff.infrastructure.persistence;

import com.statelycare.erp.staff.domain.model.Staff;
import com.statelycare.erp.staff.domain.repository.StaffRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JpaStaffRepositoryImpl implements StaffRepository {

    private final SpringDataStaffRepository repository;

    public JpaStaffRepositoryImpl(SpringDataStaffRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Staff> findAll() {
        return repository.findByDeletedAtIsNull().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Staff> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Staff save(Staff staff) {
        return toDomain(repository.save(toEntity(staff)));
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public void deleteById(UUID id) {
        repository.findById(id).ifPresent(entity -> {
            entity.setDeletedAt(java.time.Instant.now());
            entity.setActive(false);
            repository.save(entity);
        });
    }

    private Staff toDomain(StaffEntity entity) {
        return new Staff(
            entity.getId(), entity.getFirstName(), entity.getLastName(),
            entity.getProfessionalRole(), entity.getShift(), entity.getHireDate(),
            entity.isActive(), entity.getCreatedAt(), entity.getUpdatedAt()
        );
    }

    private StaffEntity toEntity(Staff domain) {
        StaffEntity entity = new StaffEntity();
        entity.setId(domain.id());
        entity.setFirstName(domain.firstName());
        entity.setLastName(domain.lastName());
        entity.setProfessionalRole(domain.professionalRole());
        entity.setShift(domain.shift());
        entity.setHireDate(domain.hireDate());
        entity.setActive(domain.isActive());
        entity.setCreatedAt(domain.createdAt());
        entity.setUpdatedAt(domain.updatedAt());
        return entity;
    }
}
