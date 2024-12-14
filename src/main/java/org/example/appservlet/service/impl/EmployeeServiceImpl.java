package org.example.appservlet.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.appservlet.dto.request.EmployeeRequestDto;
import org.example.appservlet.dto.response.EmployeeResponseDto;
import org.example.appservlet.dto.response.Response;
import org.example.appservlet.dto.response.TaskResponseDto;
import org.example.appservlet.exception.DepartmentNotFoundException;
import org.example.appservlet.exception.EmployeeNotFoundException;
import org.example.appservlet.mapper.EmployeeMapper;
import org.example.appservlet.mapper.TaskMapper;
import org.example.appservlet.model.Department;
import org.example.appservlet.model.Employee;
import org.example.appservlet.repository.DepartmentRepository;
import org.example.appservlet.repository.EmployeeRepository;
import org.example.appservlet.service.EmployeeService;
import org.example.appservlet.util.TryParse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;
import java.util.List;

import static org.example.appservlet.util.MessageHelper.SUCCESS_DELETE_MESSAGE;
import static org.example.appservlet.util.MessageHelper.SUCCESS_UPDATE_MESSAGE;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeMapper employeeMapper;
    private final TaskMapper taskMapper;

    @Override
    @Transactional
    public EmployeeResponseDto save(EmployeeRequestDto employeeRequestDto) {
        Employee employee = createEmployee(employeeRequestDto);
        employee = employeeRepository.save(employee);

        return employeeMapper.toEmployeeResponseDto(employee);
    }

    @Override
    public List<EmployeeResponseDto> findAll() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toEmployeeResponseDto)
                .sorted(Comparator.comparing(EmployeeResponseDto::getId))
                .toList();
    }

    @Override
    public EmployeeResponseDto findById(String id) {
        Integer employeeId = TryParse.Int(id);
        return employeeRepository.findById(employeeId)
                .map(employeeMapper::toEmployeeResponseDto)
                .orElseThrow(EmployeeNotFoundException::new);
    }

    @Override
    @Transactional
    public Response update(String id, EmployeeRequestDto employeeRequestDto) {
        Employee employeeNew = createEmployee(employeeRequestDto);
        employeeNew.setId(TryParse.Int(id));

        Employee employeeUpdated = fillNullFields(employeeNew);
        employeeRepository.update(employeeUpdated);
        return new Response(SUCCESS_UPDATE_MESSAGE);
    }

    @Override
    @Transactional
    public Response deleteById(String id) {
        Integer employeeId = TryParse.Int(id);
        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeNotFoundException();
        }
        employeeRepository.deleteById(employeeId);

        return new Response(SUCCESS_DELETE_MESSAGE);
    }

    @Override
    public List<TaskResponseDto> findTasksByEmployeeId(String id) {
        Integer employeeId = TryParse.Int(id);
        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeNotFoundException();
        }

        return employeeRepository.findTasksByEmployeeId(employeeId).stream()
                .map(taskMapper::toTaskResponseDto)
                .sorted(Comparator.comparing(TaskResponseDto::getId))
                .toList();
    }

    private Employee fillNullFields(Employee employee_new) {
        Employee employee_old = employeeRepository.findById(employee_new.getId())
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

    private Employee createEmployee(EmployeeRequestDto employeeRequestDto) {
        Department department = departmentRepository.findById(employeeRequestDto.getDepartmentId()).
                orElseThrow(DepartmentNotFoundException::new);

        return Employee.builder()
                .firstname(employeeRequestDto.getFirstname())
                .lastname(employeeRequestDto.getLastname())
                .email(employeeRequestDto.getEmail())
                .age(employeeRequestDto.getAge())
                .department(department)
                .build();
    }
}
