package org.example.appservlet.service;

import org.example.appservlet.dto.request.DepartmentRequestDto;
import org.example.appservlet.dto.response.DepartmentResponseDto;
import org.example.appservlet.dto.response.EmployeeResponseDto;
import org.example.appservlet.dto.response.Response;
import java.util.List;

public interface DepartmentService {
    DepartmentResponseDto save(DepartmentRequestDto departmentRequestDto);

    Response deleteById(String id);

    Response update(String id, DepartmentRequestDto departmentRequestDto);

    DepartmentResponseDto findById(String id);

    List<DepartmentResponseDto> findAll();

    List<EmployeeResponseDto> findEmployeeByDepartmentId(String id);
}
