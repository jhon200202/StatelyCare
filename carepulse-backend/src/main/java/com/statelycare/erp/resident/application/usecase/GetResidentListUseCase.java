package com.statelycare.erp.resident.application.usecase;

import com.statelycare.erp.resident.application.dto.ResidentResponse;
import com.statelycare.erp.resident.domain.repository.ResidentRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetResidentListUseCase {

    private final ResidentRepository repository;

    public GetResidentListUseCase(ResidentRepository repository) {
        this.repository = repository;
    }

    public List<ResidentResponse> execute(String query, String sortBy, String direction) {
        String finalSortBy = (sortBy == null || sortBy.isEmpty()) ? "firstName" : sortBy;
        String finalDirection = (direction == null || direction.isEmpty()) ? "asc" : direction;
        
        return repository.findAll(query, finalSortBy, finalDirection).stream()
                .map(r -> new ResidentResponse(
                        r.id(),
                        r.residentCode(),
                        r.firstName(),
                        r.lastName(),
                        r.dateOfBirth(),
                        r.gender(),
                        r.roomId(),
                        r.admissionDate(),
                        r.status()
                ))
                .collect(Collectors.toList());
    }

    public List<ResidentResponse> execute() {
        return execute(null, "firstName", "asc");
    }
}
