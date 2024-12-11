package org.example.appservlet.service.impl;

import org.example.appservlet.exception.DepartmentNotFoundException;
import org.example.appservlet.model.Department;
import org.example.appservlet.repository.DepartmentRepository;
import org.example.appservlet.service.DepartmentService;
import org.example.appservlet.dto.DepartmentDTO;
import org.example.appservlet.dto.EmployeeDTO;
import org.example.appservlet.mapper.DepartmentMapper;
import org.example.appservlet.mapper.EmployeeMapper;

import org.example.appservlet.util.TryParse;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    @Transactional
    public void save(DepartmentDTO departmentDTO) {
        Department department = DepartmentMapper.toEntity(departmentDTO);
        departmentRepository.save(department);
    }

    @Override
    public List<DepartmentDTO> findAll() {
        return departmentRepository.findAll().stream()
                .map(DepartmentMapper::toDto)
                .sorted(Comparator.comparing(DepartmentDTO::getId))
                .toList();
    }

    @Override
    public DepartmentDTO findById(String id) {
        Integer departmentId = TryParse.Int(id);
        return departmentRepository.findById(departmentId)
                .map(DepartmentMapper::toDto)
                .orElseThrow(DepartmentNotFoundException::new);
    }

    @Override
    @Transactional
    public void update(DepartmentDTO departmentDTO) throws ObjectNotFoundException {
        Department departmentNew = DepartmentMapper.toEntity(departmentDTO);
        Department departmentUpdated = fillNullFields(departmentNew);
        departmentRepository.update(departmentUpdated);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        Integer departmentId = TryParse.Int(id);
        if (!departmentRepository.existsById(departmentId)) {
            throw new DepartmentNotFoundException();
        } else {
            departmentRepository.deleteById(departmentId);
        }
    }

    @Override
    public List<EmployeeDTO> findEmployeeByDepartmentId(String id) {
        Integer departmentId = TryParse.Int(id);
        if (!departmentRepository.existsById(departmentId)) {
            throw new DepartmentNotFoundException();
        } else {
            return departmentRepository.findEmployeesByDepartmentId(departmentId).stream()
                    .map(EmployeeMapper::toDto)
                    .sorted(Comparator.comparing(EmployeeDTO::getId))
                    .toList();
        }
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
}
