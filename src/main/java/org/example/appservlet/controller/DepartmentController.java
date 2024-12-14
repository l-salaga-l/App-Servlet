package org.example.appservlet.controller;

import lombok.RequiredArgsConstructor;
import org.example.appservlet.dto.request.DepartmentRequestDto;
import org.example.appservlet.dto.response.DepartmentResponseDto;
import org.example.appservlet.dto.response.EmployeeResponseDto;
import org.example.appservlet.dto.response.Response;
import org.example.appservlet.service.DepartmentService;
import org.springframework.http.ResponseEntity;
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
@RequestMapping(value = "/department", produces = "application/json")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping(value = "/")
    public ResponseEntity<List<DepartmentResponseDto>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DepartmentResponseDto> getDepartmentById(@PathVariable("id") String id) {
        return ResponseEntity.ok(departmentService.findById(id));
    }

    @GetMapping(value = "/{id}/employees")
    public ResponseEntity<List<EmployeeResponseDto>> getEmployees(@PathVariable("id") String id) {
        return ResponseEntity.ok(departmentService.findEmployeeByDepartmentId(id));
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<Response> updateDepartment(@PathVariable("id") String id,
                                                     @RequestBody DepartmentRequestDto department) {
        return ResponseEntity.ok(departmentService.update(id, department));
    }

    @PutMapping(value = "/")
    public ResponseEntity<DepartmentResponseDto> createDepartment(@RequestBody DepartmentRequestDto department) {
        return ResponseEntity.ok(departmentService.save(department));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Response> deleteDepartment(@PathVariable("id") String id) {
        return ResponseEntity.ok(departmentService.deleteById(id));
    }
}
