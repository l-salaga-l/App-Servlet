package org.example.appservlet.controller;

import lombok.RequiredArgsConstructor;
import org.example.appservlet.dto.response.Response;
import org.example.appservlet.dto.request.EmployeeRequestDto;
import org.example.appservlet.dto.response.EmployeeResponseDto;
import org.example.appservlet.dto.response.TaskResponseDto;
import org.example.appservlet.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@RestController
@RequestMapping(value = "/employee", produces="application/json")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping(value = "/")
    public ResponseEntity<List<EmployeeResponseDto>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeById(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(employeeService.findById(id));
    }

    @GetMapping(value = "/{id}/tasks")
    public ResponseEntity<List<TaskResponseDto>> getEmployeeTasks(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(employeeService.findTasksByEmployeeId(id));
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<Response> updateEmployee(@PathVariable(value = "id") String id,
                                                   @RequestBody @Validated EmployeeRequestDto employee) {
        return ResponseEntity.ok(employeeService.update(id, employee));
    }

    @PutMapping(value = "/")
    public ResponseEntity<EmployeeResponseDto> createEmployee(@RequestBody @Validated EmployeeRequestDto employee) {
        return ResponseEntity.ok(employeeService.save(employee));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Response> deleteEmployee(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(employeeService.deleteById(id));
    }
}
