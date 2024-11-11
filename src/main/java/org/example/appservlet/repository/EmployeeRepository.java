package org.example.appservlet.repository;

import org.example.appservlet.model.Department;
import org.example.appservlet.model.Employee;
import org.example.appservlet.model.Task;

import java.util.Optional;

public interface EmployeeRepository extends Repository<Employee, Integer> {
    Optional<Department> findDepartmentByEmployeeId(Integer employeeId);

    Iterable<Task> findTasksByEmployeeId(Integer employeeId);
}
