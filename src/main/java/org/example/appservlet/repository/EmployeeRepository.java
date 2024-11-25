package org.example.appservlet.repository;

import org.example.appservlet.model.Employee;
import org.example.appservlet.model.Task;

public interface EmployeeRepository extends Repository<Employee, Integer> {
    Iterable<Task> findTasksByEmployeeId(Integer employeeId);
}
