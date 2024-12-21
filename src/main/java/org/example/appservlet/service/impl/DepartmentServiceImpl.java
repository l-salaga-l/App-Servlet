package org.example.appservlet.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.appservlet.dto.request.DepartmentRequestDto;
import org.example.appservlet.dto.response.DepartmentResponseDto;
import org.example.appservlet.dto.response.EmployeeResponseDto;
import org.example.appservlet.dto.response.Response;
import org.example.appservlet.exception.DepartmentNotFoundException;
import org.example.appservlet.mapper.DepartmentMapper;
import org.example.appservlet.mapper.EmployeeMapper;
import org.example.appservlet.model.Department;
import org.example.appservlet.repository.DepartmentRepository;
import org.example.appservlet.service.DepartmentService;
import org.example.appservlet.util.TryParse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;
import java.util.List;

import static org.example.appservlet.util.MessageHelper.SUCCESS_DELETE_MESSAGE;
import static org.example.appservlet.util.MessageHelper.SUCCESS_UPDATE_MESSAGE;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final EmployeeMapper employeeMapper;

    @Override
    @Transactional
    public DepartmentResponseDto save(DepartmentRequestDto departmentRequestDto) {
        Department department = createDepartment(departmentRequestDto);
        department = departmentRepository.save(department);

        return departmentMapper.toDepartmentResponseDto(department);
    }

    @Override
    public List<DepartmentResponseDto> findAll() {
        return departmentRepository.findAll().stream()
                .map(departmentMapper::toDepartmentResponseDto)
                .sorted(Comparator.comparing(DepartmentResponseDto::getId))
                .toList();
    }

    @Override
    public DepartmentResponseDto findById(String id) {
        Integer departmentId = TryParse.Int(id);
        return departmentRepository.findById(departmentId)
                .map(departmentMapper::toDepartmentResponseDto)
                .orElseThrow(DepartmentNotFoundException::new);
    }

    @Override
    @Transactional
    public Response update(String id, DepartmentRequestDto departmentRequestDto) {
        Department departmentNew = createDepartment(departmentRequestDto);
        departmentNew.setId(TryParse.Int(id));

        Department departmentUpdated = fillNullFields(departmentNew);
        departmentRepository.update(departmentUpdated);
        return new Response(SUCCESS_UPDATE_MESSAGE);
    }

    @Override
    @Transactional
    public Response deleteById(String id) {
        Integer departmentId = TryParse.Int(id);
        if (!departmentRepository.existsById(departmentId)) {
            throw new DepartmentNotFoundException();
        }
        departmentRepository.deleteById(departmentId);
        return new Response(SUCCESS_DELETE_MESSAGE);
    }

    @Override
    public List<EmployeeResponseDto> findEmployeeByDepartmentId(String id) {
        Integer departmentId = TryParse.Int(id);
        if (!departmentRepository.existsById(departmentId)) {
            throw new DepartmentNotFoundException();
        }
        return departmentRepository.findEmployeesByDepartmentId(departmentId).stream()
                .map(employeeMapper::toEmployeeResponseDto)
                .sorted(Comparator.comparing(EmployeeResponseDto::getId))
                .toList();
    }

    private Department fillNullFields(Department department_new) {
        Department department_old = departmentRepository.findById(department_new.getId()).orElseThrow(DepartmentNotFoundException::new);

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

    private Department createDepartment(DepartmentRequestDto departmentRequestDto) {
        return Department.builder()
                .departmentName(departmentRequestDto.getDepartmentName())
                .location(departmentRequestDto.getLocation())
                .build();
    }
}
