package org.example.appservlet.controller;

import lombok.RequiredArgsConstructor;
import org.example.appservlet.dto.request.TaskRequestDto;
import org.example.appservlet.dto.response.EmployeeResponseDto;
import org.example.appservlet.dto.response.Response;
import org.example.appservlet.dto.response.TaskResponseDto;
import org.example.appservlet.service.TaskService;
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
@RequestMapping(value = "/task", produces = "application/json")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping(value = "/")
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        return ResponseEntity.ok(taskService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(taskService.findById(id));
    }

    @GetMapping(value = "/{id}/employees")
    public ResponseEntity<List<EmployeeResponseDto>> getEmployees(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(taskService.findEmployeesByTaskId(id));
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<Response> updateTask(@PathVariable(value = "id") String id,
                                               @RequestBody @Validated TaskRequestDto taskRequestDto) {
        return ResponseEntity.ok(taskService.update(id, taskRequestDto));
    }

    @PutMapping(value = "/")
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody @Validated TaskRequestDto taskRequestDto) {
        return ResponseEntity.ok(taskService.save(taskRequestDto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Response> deleteTask(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(taskService.deleteById(id));
    }
}
