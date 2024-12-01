package org.example.appservlet.service;

import org.example.appservlet.dto.DepartmentDTO;
import org.example.appservlet.dto.EmployeeDTO;

import java.util.List;

public interface DepartmentService extends Service<DepartmentDTO> {
    List<EmployeeDTO> findEmployeeByDepartmentId(String id);
}
