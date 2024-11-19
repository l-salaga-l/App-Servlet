package org.example.appservlet.service;

import org.example.appservlet.exception.NotFoundException;
import org.example.appservlet.model.Employee;
import org.example.appservlet.model.Task;

public interface EmployeeService extends Service<Employee> {
    Iterable<Task> findTasksByEmployeeId(Integer employeeId) throws NotFoundException;
}
