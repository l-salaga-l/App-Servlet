package org.example.appservlet.service;

import org.example.appservlet.dto.request.TaskRequestDto;
import org.example.appservlet.dto.response.EmployeeResponseDto;
import org.example.appservlet.dto.response.Response;
import org.example.appservlet.dto.response.TaskResponseDto;
import java.util.List;

public interface TaskService {
    TaskResponseDto save(TaskRequestDto taskRequestDto);

    Response deleteById(String id);

    Response update(String id, TaskRequestDto taskRequestDto);

    TaskResponseDto findById(String id);

    List<TaskResponseDto> findAll();

    List<EmployeeResponseDto> findEmployeesByTaskId(String id);
}
