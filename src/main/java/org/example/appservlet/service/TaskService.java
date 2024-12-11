package org.example.appservlet.service;

import org.example.appservlet.dto.EmployeeDTO;
import org.example.appservlet.dto.TaskDTO;

import java.util.List;

public interface TaskService extends Service<TaskDTO> {
    List<EmployeeDTO> findEmployeesByTaskId(String id);
}
