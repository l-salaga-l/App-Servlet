package org.example.appservlet.service;

import org.example.appservlet.service.dto.EmployeeDTO;
import org.example.appservlet.service.dto.TaskDTO;
import org.hibernate.ObjectNotFoundException;

public interface TaskService extends Service<TaskDTO> {
    Iterable<EmployeeDTO> findEmployeesByTaskId(Integer taskId) throws ObjectNotFoundException;
}
