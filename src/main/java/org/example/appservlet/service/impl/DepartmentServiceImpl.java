package org.example.appservlet.service.impl;

import org.example.appservlet.exception.NotFoundException;
import org.example.appservlet.model.Department;
import org.example.appservlet.model.Employee;
import org.example.appservlet.repository.DepartmentRepository;
import org.example.appservlet.service.DepartmentService;

import java.util.ArrayList;

public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public Iterable<Department> findAll() {
        return departmentRepository.findAll();
    }

    @Override
    public Department findById(Integer id) throws NotFoundException {
        return departmentRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public void update(Department department) throws NotFoundException {
        try {
            checkExist(department.getId());
            departmentRepository.update(fillNullFields(department));
        } catch (NotFoundException e) {
            throw new NotFoundException();
        }
    }

    @Override
    public void deleteById(Integer id) throws NotFoundException {
        try {
            checkExist(id);
            departmentRepository.deleteById(id);
        } catch (NotFoundException e) {
            throw new NotFoundException();
        }
    }

    @Override
    public Iterable<Employee> findEmployeeByDepartmentId(Integer departmentId) throws NotFoundException {
        try {
            checkExist(departmentId);
        } catch (NotFoundException e) {
            throw new NotFoundException();
        }
        return departmentRepository.findEmployeesByDepartmentId(departmentId);
    }

    private void checkExist(Integer id) throws NotFoundException {
        if (!departmentRepository.existsById(id)) {
            throw new NotFoundException();
        }
    }

    private Department fillNullFields(Department department_new) throws NotFoundException {
        Department department_old = findById(department_new.getId());

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
