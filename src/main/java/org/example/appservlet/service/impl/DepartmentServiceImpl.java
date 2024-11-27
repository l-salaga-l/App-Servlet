package org.example.appservlet.service.impl;

import org.example.appservlet.model.Department;
import org.example.appservlet.model.Employee;
import org.example.appservlet.repository.DepartmentRepository;
import org.example.appservlet.service.DepartmentService;
import org.example.appservlet.service.dto.DepartmentDTO;
import org.example.appservlet.service.dto.EmployeeDTO;
import org.example.appservlet.service.mapper.DepartmentMapper;
import org.example.appservlet.service.mapper.EmployeeMapper;

import org.hibernate.ObjectNotFoundException;

public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public DepartmentDTO save(DepartmentDTO departmentDTO) {
        Department department = DepartmentMapper.toEntity(departmentDTO);
        Department savedDepartment = departmentRepository.save(department);
        return DepartmentMapper.toDto(savedDepartment);
    }

    @Override
    public Iterable<DepartmentDTO> findAll() {
        Iterable<Department> departments = departmentRepository.findAll();
        return DepartmentMapper.toDto(departments);
    }

    @Override
    public DepartmentDTO findById(Integer id) throws ObjectNotFoundException {
        Department department = departmentRepository.findById(id).orElseThrow();
        return DepartmentMapper.toDto(department);
    }

    @Override
    public void update(DepartmentDTO departmentDTO) throws ObjectNotFoundException {
        Department departmentNew = DepartmentMapper.toEntity(departmentDTO);
        Department departmentUpdated = fillNullFields(departmentNew);
        departmentRepository.save(departmentUpdated);
    }

    @Override
    public void deleteById(Integer id) throws ObjectNotFoundException {
        departmentRepository.deleteById(id);
    }

    @Override
    public Iterable<EmployeeDTO> findEmployeeByDepartmentId(Integer departmentId) throws ObjectNotFoundException {
        Iterable<Employee> employees = departmentRepository.findEmployeesByDepartmentId(departmentId);
        return EmployeeMapper.toDto(employees);
    }

    private Department fillNullFields(Department department_new) throws ObjectNotFoundException {
        Department department_old = departmentRepository.findById(department_new.getId()).orElseThrow();

        if (department_new.getDepartmentName() == null) {
            department_new.setDepartmentName(department_old.getDepartmentName());
        }
        if (department_new.getLocation() == null) {
            department_new.setLocation(department_old.getLocation());
        }
        if (department_new.getEmployees() == null) {
            department_new.setEmployees(department_old.getEmployees());
        }

        return department_old;
    }
}
