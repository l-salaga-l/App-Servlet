package org.example.appservlet.service.impl;

import org.example.appservlet.exception.EmployeeNotFoundException;
import org.example.appservlet.model.Employee;
import org.example.appservlet.repository.EmployeeRepository;
import org.example.appservlet.service.EmployeeService;
import org.example.appservlet.dto.EmployeeDTO;
import org.example.appservlet.dto.TaskDTO;
import org.example.appservlet.mapper.EmployeeMapper;
import org.example.appservlet.mapper.TaskMapper;

import org.example.appservlet.util.TryParse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository repository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = EmployeeMapper.toEntity(employeeDTO);
        repository.save(employee);
    }

    @Override
    public List<EmployeeDTO> findAll() {
        return repository.findAll().stream()
                .map(EmployeeMapper::toDto)
                .sorted(Comparator.comparing(EmployeeDTO::getId))
                .toList();
    }

    @Override
    public EmployeeDTO findById(String id) {
        Integer employeeId = TryParse.Int(id);
        return repository.findById(employeeId)
                .map(EmployeeMapper::toDto)
                .orElseThrow(EmployeeNotFoundException::new);
    }

    @Override
    @Transactional
    public void update(EmployeeDTO employeeDTO) {
        Employee employeeNew = EmployeeMapper.toEntity(employeeDTO);
        Employee employeeUpdated = fillNullFields(employeeNew);
        repository.update(employeeUpdated);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        Integer employeeId = TryParse.Int(id);
        if (!repository.existsById(employeeId)) {
            throw new EmployeeNotFoundException();
        } else {
            repository.deleteById(employeeId);
        }
    }

    @Override
    public List<TaskDTO> findTasksByEmployeeId(String id) {
        Integer employeeId = TryParse.Int(id);
        if (!repository.existsById(employeeId)) {
            throw new EmployeeNotFoundException();
        } else {
            return repository.findTasksByEmployeeId(employeeId).stream()
                    .map(TaskMapper::toDto)
                    .sorted(Comparator.comparing(TaskDTO::getId))
                    .toList();
        }
    }

    private Employee fillNullFields(Employee employee_new) {
        Employee employee_old = repository.findById(employee_new.getId())
                .orElseThrow(EmployeeNotFoundException::new);

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

        return employee_new;
    }
}
