package org.example.appservlet.controller;

import lombok.RequiredArgsConstructor;
import org.example.appservlet.dto.EmployeeDTO;
import org.example.appservlet.dto.Response;
import org.example.appservlet.dto.TaskDTO;
import org.example.appservlet.service.TaskService;
import org.example.appservlet.util.TryParse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/task", produces = "application/json")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskController {
    private final TaskService taskService;

    @GetMapping(value = "/")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        return new ResponseEntity<>(taskService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable(value = "id") String id) {
        return new ResponseEntity<>(taskService.findById(id), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/employees")
    public ResponseEntity<List<EmployeeDTO>> getEmployees(@PathVariable(value = "id") String id) {
        return new ResponseEntity<>(taskService.findEmployeesByTaskId(id), HttpStatus.OK);
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<Response> updateTask(@PathVariable(value = "id") String id, @RequestBody TaskDTO taskDTO) {
        taskDTO.setId(TryParse.Int(id));
        taskService.update(taskDTO);
        return new ResponseEntity<>(new Response("Данные успешно обновлены.", HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping(value = "/")
    public ResponseEntity<Response> createTask(@RequestBody TaskDTO taskDTO) {
        taskService.save(taskDTO);
        return new ResponseEntity<>(new Response("Запись успешно сохранена.", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Response> deleteTask(@PathVariable(value = "id") String id) {
        taskService.deleteById(id);
        return new ResponseEntity<>(new Response("Запись успешно удалена.", HttpStatus.OK), HttpStatus.OK);
    }
}
