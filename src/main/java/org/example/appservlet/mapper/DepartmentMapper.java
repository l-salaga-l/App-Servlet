package org.example.appservlet.mapper;

import org.example.appservlet.dto.response.DepartmentResponseDto;
import org.example.appservlet.model.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "departmentName", target = "departmentName")
    @Mapping(source = "location", target = "location")
    DepartmentResponseDto toDepartmentResponseDto(Department department);
}
