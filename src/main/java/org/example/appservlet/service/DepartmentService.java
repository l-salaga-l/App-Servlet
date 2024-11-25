package org.example.appservlet.service;

import org.example.appservlet.service.dto.DepartmentDTO;
import org.example.appservlet.service.dto.EmployeeDTO;
import org.hibernate.ObjectNotFoundException;

public interface DepartmentService extends Service<DepartmentDTO> {
    Iterable<EmployeeDTO> findEmployeeByDepartmentId(Integer departmentId) throws ObjectNotFoundException;
}
