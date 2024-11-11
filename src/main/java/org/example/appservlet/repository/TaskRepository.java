package org.example.appservlet.repository;

import org.example.appservlet.model.Employee;
import org.example.appservlet.model.Task;

public interface TaskRepository extends Repository<Task, Integer> {
    Iterable<Employee> findEmployeesByTaskId(Integer taskId);
}
