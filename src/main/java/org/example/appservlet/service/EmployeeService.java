package org.example.appservlet.service;

import org.example.appservlet.dto.EmployeeDTO;
import org.example.appservlet.dto.TaskDTO;

import java.util.List;

public interface EmployeeService extends Service<EmployeeDTO> {
    List<TaskDTO> findTasksByEmployeeId(String id);
}
