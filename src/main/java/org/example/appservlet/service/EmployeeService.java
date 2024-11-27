package org.example.appservlet.service;

import org.example.appservlet.service.dto.EmployeeDTO;
import org.example.appservlet.service.dto.TaskDTO;
import org.hibernate.ObjectNotFoundException;

public interface EmployeeService extends Service<EmployeeDTO> {
    Iterable<TaskDTO> findTasksByEmployeeId(Integer employeeId) throws ObjectNotFoundException;
}
