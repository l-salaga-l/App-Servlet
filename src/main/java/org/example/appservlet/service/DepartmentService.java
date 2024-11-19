package org.example.appservlet.service;

import org.example.appservlet.exception.NotFoundException;
import org.example.appservlet.model.Department;
import org.example.appservlet.model.Employee;

public interface DepartmentService extends Service<Department> {
    Iterable<Employee> findEmployeeByDepartmentId(Integer departmentId) throws NotFoundException;
}
