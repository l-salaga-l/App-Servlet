package org.example.appservlet.repository;

import org.example.appservlet.model.Department;
import org.example.appservlet.model.Employee;

public interface DepartmentRepository extends Repository<Department, Integer> {
    Iterable<Employee> findEmployeesByDepartmentId(Integer departmentId);
}
