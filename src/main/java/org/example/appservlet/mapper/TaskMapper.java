package org.example.appservlet.mapper;

import org.example.appservlet.dto.response.TaskResponseDto;
import org.example.appservlet.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "taskName", target = "taskName")
    @Mapping(source = "deadline", target = "deadline")
    TaskResponseDto toTaskResponseDto(Task task);
}
