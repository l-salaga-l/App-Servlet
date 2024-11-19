package org.example.appservlet.service;

import org.example.appservlet.exception.NotFoundException;
import org.example.appservlet.model.Employee;
import org.example.appservlet.model.Task;

public interface TaskService extends Service<Task> {
    Iterable<Employee> findEmployeesByTaskId(Integer taskId) throws NotFoundException;
}
