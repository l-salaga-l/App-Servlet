package org.example.appservlet.mapper;

import org.example.appservlet.dto.response.EmployeeResponseDto;
import org.example.appservlet.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "firstname", target = "firstname")
    @Mapping(source = "lastname", target = "lastname")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "age", target = "age")
    EmployeeResponseDto toEmployeeResponseDto(Employee employee);
}
