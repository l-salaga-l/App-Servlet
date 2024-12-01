package org.example.appservlet.mapper;

import org.example.appservlet.model.Task;
import org.example.appservlet.dto.TaskDTO;

import java.util.ArrayList;
import java.util.List;

public class TaskMapper {

    public static TaskDTO toDto(Task task) {
        if (task == null) {
            return null;
        }
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTaskName(task.getTaskName());
        dto.setDeadline(task.getDeadline());
        return dto;
    }

    public static Iterable<TaskDTO> toDto(Iterable<Task> tasks) {
        if (tasks == null) {
            return null;
        }

        List<TaskDTO> taskDTOs = new ArrayList<TaskDTO>();
        for (Task task : tasks) {
            taskDTOs.add(toDto(task));
        }

        return taskDTOs;
    }

    public static Task toEntity(TaskDTO dto) {
        if (dto == null) {
            return null;
        }
        Task task = new Task();
        task.setId(dto.getId());
        task.setTaskName(dto.getTaskName());
        task.setDeadline(dto.getDeadline());
        return task;
    }
}
