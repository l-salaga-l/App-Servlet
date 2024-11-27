package org.example.appservlet.service.mapper;

import org.example.appservlet.model.Employee;
import org.example.appservlet.service.dto.EmployeeDTO;

import java.util.ArrayList;
import java.util.List;

public class EmployeeMapper {

    public static EmployeeDTO toDto(Employee employee) {
        if (employee == null) {
            return null;
        }
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setFirstname(employee.getFirstname());
        dto.setLastname(employee.getLastname());
        dto.setEmail(employee.getEmail());
        dto.setAge(employee.getAge());
        return dto;
    }

    public static Iterable<EmployeeDTO> toDto(Iterable<Employee> employees) {
        if (employees == null) {
            return null;
        }
        List<EmployeeDTO> employeeDTOS = new ArrayList<>();

        for (Employee employee : employees) {
            employeeDTOS.add(toDto(employee));
        }

        return employeeDTOS;
    }

    public static Employee toEntity(EmployeeDTO dto) {
        if (dto == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(dto.getId());
        employee.setFirstname(dto.getFirstname());
        employee.setLastname(dto.getLastname());
        employee.setEmail(dto.getEmail());
        employee.setAge(dto.getAge());
        employee.setDepartment(null);
        employee.setTasks(null);
        return employee;
    }
}
