package org.example.appservlet.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.appservlet.dto.request.TaskRequestDto;
import org.example.appservlet.dto.response.EmployeeResponseDto;
import org.example.appservlet.dto.response.Response;
import org.example.appservlet.dto.response.TaskResponseDto;
import org.example.appservlet.exception.TaskNotFoundException;
import org.example.appservlet.mapper.EmployeeMapper;
import org.example.appservlet.mapper.TaskMapper;
import org.example.appservlet.model.Task;
import org.example.appservlet.repository.TaskRepository;
import org.example.appservlet.service.TaskService;
import org.example.appservlet.util.TryParse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;
import java.util.List;

import static org.example.appservlet.util.MessageHelper.SUCCESS_DELETE_MESSAGE;
import static org.example.appservlet.util.MessageHelper.SUCCESS_UPDATE_MESSAGE;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final EmployeeMapper employeeMapper;

    @Override
    @Transactional
    public TaskResponseDto save(TaskRequestDto taskRequestDto) {
        Task task = createTask(taskRequestDto);
        task = taskRepository.save(task);

        return taskMapper.toTaskResponseDto(task);
    }

    @Override
    public List<TaskResponseDto> findAll() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toTaskResponseDto)
                .sorted(Comparator.comparing(TaskResponseDto::getId))
                .toList();
    }

    @Override
    public TaskResponseDto findById(String id) {
        Integer taskId = TryParse.Int(id);
        return taskRepository.findById(taskId)
                .map(taskMapper::toTaskResponseDto)
                .orElseThrow(TaskNotFoundException::new);
    }

    @Override
    @Transactional
    public Response update(String id, TaskRequestDto taskRequestDto) {
        Task task = createTask(taskRequestDto);
        task.setId(TryParse.Int(id));

        Task taskUpdated = fillNullFields(task);
        taskRepository.update(taskUpdated);
        return new Response(SUCCESS_UPDATE_MESSAGE);
    }

    @Override
    @Transactional
    public Response deleteById(String id) {
        Integer taskId = TryParse.Int(id);
        if (!taskRepository.existsById(taskId)) {
            throw new TaskNotFoundException();
        }
        taskRepository.deleteById(taskId);
        return new Response(SUCCESS_DELETE_MESSAGE);
    }

    @Override
    public List<EmployeeResponseDto> findEmployeesByTaskId(String id) {
        Integer taskId = TryParse.Int(id);
        if (!taskRepository.existsById(taskId)) {
            throw new TaskNotFoundException();
        }

        return taskRepository.findEmployeesByTaskId(taskId).stream()
                .map(employeeMapper::toEmployeeResponseDto)
                .sorted(Comparator.comparing(EmployeeResponseDto::getId))
                .toList();
    }

    private Task fillNullFields(Task task_new) {
        Task task_old = taskRepository.findById(task_new.getId()).orElseThrow();

        if (task_new.getTaskName() == null) {
            task_new.setTaskName(task_old.getTaskName());
        }
        if (task_new.getDeadline() == null) {
            task_new.setDeadline(task_old.getDeadline());
        }
        if (task_new.getEmployees() == null) {
            task_new.setEmployees(task_old.getEmployees());
        }

        return task_new;
    }

    private Task createTask(TaskRequestDto taskRequestDto) {
        return Task.builder()
                .taskName(taskRequestDto.getTaskName())
                .deadline(taskRequestDto.getDeadline())
                .build();
    }
}
