package com.statelycare.erp.employee.infrastructure.web;

import com.statelycare.erp.employee.application.dto.EmployeeRequest;
import com.statelycare.erp.employee.application.dto.EmployeeResponse;
import com.statelycare.erp.employee.application.usecase.CreateEmployeeUseCase;
import com.statelycare.erp.employee.application.usecase.GetEmployeeListUseCase;
import com.statelycare.erp.employee.application.usecase.UpdateEmployeeUseCase;
import com.statelycare.erp.employee.application.usecase.DeleteEmployeeUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final GetEmployeeListUseCase getEmployeeListUseCase;
    private final CreateEmployeeUseCase createEmployeeUseCase;
    private final UpdateEmployeeUseCase updateEmployeeUseCase;
    private final DeleteEmployeeUseCase deleteEmployeeUseCase;

    public EmployeeController(
            GetEmployeeListUseCase getEmployeeListUseCase,
            CreateEmployeeUseCase createEmployeeUseCase,
            UpdateEmployeeUseCase updateEmployeeUseCase,
            DeleteEmployeeUseCase deleteEmployeeUseCase
    ) {
        this.getEmployeeListUseCase = getEmployeeListUseCase;
        this.createEmployeeUseCase = createEmployeeUseCase;
        this.updateEmployeeUseCase = updateEmployeeUseCase;
        this.deleteEmployeeUseCase = deleteEmployeeUseCase;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees(
            @RequestParam(required = false) String query,
            @RequestParam(required = false, defaultValue = "firstName") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction) {
        return ResponseEntity.ok(getEmployeeListUseCase.execute(query, sortBy, direction));
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest request) {
        return new ResponseEntity<>(createEmployeeUseCase.execute(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(@PathVariable("id") String id, @Valid @RequestBody EmployeeRequest request) {
        // El caso de uso debe aceptar el id y el request
        EmployeeResponse updated = updateEmployeeUseCase.execute(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") String id) {
        deleteEmployeeUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
