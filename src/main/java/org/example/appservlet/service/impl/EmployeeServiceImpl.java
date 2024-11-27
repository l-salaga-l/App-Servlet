package org.example.appservlet.service.impl;

import org.example.appservlet.model.Employee;
import org.example.appservlet.model.Task;
import org.example.appservlet.repository.EmployeeRepository;
import org.example.appservlet.repository.impl.EmployeeRepositoryImpl;
import org.example.appservlet.service.EmployeeService;
import org.example.appservlet.service.dto.EmployeeDTO;
import org.example.appservlet.service.dto.TaskDTO;
import org.example.appservlet.service.mapper.EmployeeMapper;
import org.example.appservlet.service.mapper.TaskMapper;

import org.hibernate.ObjectNotFoundException;

public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository repository;

    public EmployeeServiceImpl(EmployeeRepositoryImpl repository) {
        this.repository = repository;
    }

    @Override
    public EmployeeDTO save(EmployeeDTO employeeDTO) {
        Employee employee = EmployeeMapper.toEntity(employeeDTO);
        Employee savedEmployee = repository.save(employee);
        return EmployeeMapper.toDto(savedEmployee);
    }

    @Override
    public Iterable<EmployeeDTO> findAll() {
        Iterable<Employee> employees = repository.findAll();
        return EmployeeMapper.toDto(employees);
    }

    @Override
    public EmployeeDTO findById(Integer id) throws ObjectNotFoundException {
        Employee employee = repository.findById(id).orElseThrow();
        return EmployeeMapper.toDto(employee);
    }

    @Override
    public void update(EmployeeDTO employeeDTO) throws ObjectNotFoundException {
        Employee employeeNew = EmployeeMapper.toEntity(employeeDTO);
        Employee employeeUpdated = fillNullFields(employeeNew);
        repository.update(employeeUpdated);
    }

    @Override
    public void deleteById(Integer id) throws ObjectNotFoundException {
        repository.deleteById(id);
    }

    @Override
    public Iterable<TaskDTO> findTasksByEmployeeId(Integer employeeId) throws ObjectNotFoundException {
        Iterable<Task> tasks = repository.findTasksByEmployeeId(employeeId);
        return TaskMapper.toDto(tasks);
    }

    private Employee fillNullFields(Employee employee_new) throws ObjectNotFoundException {
        Employee employee_old = repository.findById(employee_new.getId()).orElseThrow();

        if (employee_new.getFirstname() == null) {
            employee_new.setFirstname(employee_old.getFirstname());
        }
        if (employee_new.getLastname() == null) {
            employee_new.setLastname(employee_old.getLastname());
        }
        if (employee_new.getEmail() == null) {
            employee_new.setEmail(employee_old.getEmail());
        }
        if (employee_new.getAge() == 0) {
            employee_new.setAge(employee_old.getAge());
        }
        if (employee_new.getDepartment() == null) {
            employee_new.setDepartment(employee_old.getDepartment());
        }
        if (employee_new.getTasks() == null) {
            employee_new.setTasks(employee_old.getTasks());
        }

        return employee_new;
    }
}
