package org.example.appservlet.controller;

import lombok.RequiredArgsConstructor;
import org.example.appservlet.dto.DepartmentDTO;
import org.example.appservlet.dto.EmployeeDTO;
import org.example.appservlet.dto.Response;
import org.example.appservlet.service.DepartmentService;
import org.example.appservlet.util.TryParse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/department", produces = "application/json")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping(value = "/")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        return new ResponseEntity<>(departmentService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable("id") String id) {
        return new ResponseEntity<>(departmentService.findById(id), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/employees")
    public ResponseEntity<List<EmployeeDTO>> getEmployees(@PathVariable("id") String id) {
        return new ResponseEntity<>(departmentService.findEmployeeByDepartmentId(id), HttpStatus.OK);
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<Response> updateDepartment(@PathVariable("id") String id, @RequestBody DepartmentDTO departmentDTO) {
        departmentDTO.setId(TryParse.Int(id));
        departmentService.update(departmentDTO);
        return new ResponseEntity<>(new Response("Данные успешно обновлены.", HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping(value = "/")
    public ResponseEntity<Response> createDepartment(@RequestBody DepartmentDTO departmentDTO) {
        departmentService.save(departmentDTO);
        return new ResponseEntity<>(new Response("Запись успешно сохранена.", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Response> deleteDepartment(@PathVariable("id") String id) {
        departmentService.deleteById(id);
        return new ResponseEntity<>(new Response("Запись успешно удалена.", HttpStatus.OK), HttpStatus.OK);
    }
}
