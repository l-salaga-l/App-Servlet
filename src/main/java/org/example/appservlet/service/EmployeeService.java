package org.example.appservlet.service;

import org.example.appservlet.dto.request.EmployeeRequestDto;
import org.example.appservlet.dto.response.EmployeeResponseDto;
import org.example.appservlet.dto.response.Response;
import org.example.appservlet.dto.response.TaskResponseDto;
import java.util.List;

public interface EmployeeService {
    EmployeeResponseDto save(EmployeeRequestDto employeeRequestDto);

    Response deleteById(String id);

    Response update(String id, EmployeeRequestDto employeeRequestDto);

    EmployeeResponseDto findById(String id);

    List<EmployeeResponseDto> findAll();

    List<TaskResponseDto> findTasksByEmployeeId(String id);
}
