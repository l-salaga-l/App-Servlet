package org.example.appservlet.service.impl;

import org.example.appservlet.exception.NotFoundException;
import org.example.appservlet.model.Employee;
import org.example.appservlet.model.Task;
import org.example.appservlet.repository.EmployeeRepository;
import org.example.appservlet.repository.impl.EmployeeRepositoryImpl;
import org.example.appservlet.service.EmployeeService;

public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository repository;

    public EmployeeServiceImpl(EmployeeRepositoryImpl repository) {
        this.repository = repository;
    }

    @Override
    public Employee save(Employee employee) {
        return repository.save(employee);
    }

    @Override
    public Iterable<Employee> findAll() {
        return repository.findAll();
    }

    @Override
    public Employee findById(Integer id) throws NotFoundException {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public void update(Employee employee) throws NotFoundException {
        try {
            checkExist(employee.getId());
            repository.update(fillNullFields(employee));
        } catch (NotFoundException e) {
            throw new NotFoundException();
        }
    }

    @Override
    public void deleteById(Integer id) throws NotFoundException {
        try {
            checkExist(id);
            repository.deleteById(id);
        } catch (NotFoundException e) {
            throw new NotFoundException();
        }
    }

    @Override
    public Iterable<Task> findTasksByEmployeeId(Integer employeeId) throws NotFoundException {
        try {
            checkExist(employeeId);
        } catch (NotFoundException e) {
            throw new NotFoundException();
        }
        return repository.findTasksByEmployeeId(employeeId);
    }

    private void checkExist(Integer id) throws NotFoundException {
        if (!repository.existsById(id)) {
            throw new NotFoundException();
        }
    }

    private Employee fillNullFields(Employee employee_new) throws NotFoundException {
        Employee employee_old = findById(employee_new.getId());

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
