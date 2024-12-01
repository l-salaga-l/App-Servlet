package org.example.appservlet.mapper;

import org.example.appservlet.model.Department;
import org.example.appservlet.dto.DepartmentDTO;

import java.util.ArrayList;
import java.util.List;

public class DepartmentMapper {
    public static DepartmentDTO toDto(Department department) {
        if (department == null) {
            return null;
        }
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(department.getId());
        departmentDTO.setDepartmentName(department.getDepartmentName());
        departmentDTO.setLocation(department.getLocation());

        return departmentDTO;
    }

    public static Iterable<DepartmentDTO> toDto(Iterable<Department> departments) {
        if (departments == null) {
            return null;
        }
        List<DepartmentDTO> departmentDTOs = new ArrayList<DepartmentDTO>();
        for (Department department : departments) {
            departmentDTOs.add(toDto(department));
        }
        return departmentDTOs;
    }

    public static Department toEntity(DepartmentDTO departmentDTO) {
        if (departmentDTO == null) {
            return null;
        }

        Department department = new Department();
        department.setId(departmentDTO.getId());
        department.setDepartmentName(departmentDTO.getDepartmentName());
        department.setLocation(departmentDTO.getLocation());
        department.setEmployees(null);

        return department;
    }
}
