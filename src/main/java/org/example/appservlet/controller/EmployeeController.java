package org.example.appservlet.controller;

import lombok.RequiredArgsConstructor;
import org.example.appservlet.dto.TaskDTO;
import org.example.appservlet.dto.Response;
import org.example.appservlet.service.EmployeeService;
import org.example.appservlet.dto.EmployeeDTO;
import org.example.appservlet.util.TryParse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping(value = "/employee", produces="application/json")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping(value = "/")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return new ResponseEntity<>(employeeService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable(value = "id") String id) {
        return new ResponseEntity<>(employeeService.findById(id), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/tasks")
    public ResponseEntity<List<TaskDTO>> getEmployeeTasks(@PathVariable(value = "id") String id) {
        return new ResponseEntity<>(employeeService.findTasksByEmployeeId(id), HttpStatus.OK);
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<Response> updateEmployee(@PathVariable(value = "id") String id, @RequestBody EmployeeDTO employeeDTO) {
        employeeDTO.setId(TryParse.Int(id));
        employeeService.update(employeeDTO);
        return new ResponseEntity<>(new Response("Данные успешно обновлены.", HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping(value = "/")
    public ResponseEntity<Response> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        employeeService.save(employeeDTO);
        return new ResponseEntity<>(new Response("Запись успешно сохранена.", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Response> deleteEmployee(@PathVariable(value = "id") String id) {
        employeeService.deleteById(id);
        return new ResponseEntity<>(new Response("Запись успешно удалена.", HttpStatus.OK), HttpStatus.OK);
    }
}
