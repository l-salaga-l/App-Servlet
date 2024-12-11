package org.example.appservlet.service.impl;

import org.example.appservlet.exception.TaskNotFoundException;
import org.example.appservlet.model.Task;
import org.example.appservlet.repository.TaskRepository;
import org.example.appservlet.service.TaskService;
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
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    @Transactional
    public void save(TaskDTO taskDTO) {
        Task task = TaskMapper.toEntity(taskDTO);
        taskRepository.save(task);
    }

    @Override
    public List<TaskDTO> findAll() {
        return taskRepository.findAll().stream()
                .map(TaskMapper::toDto)
                .sorted(Comparator.comparing(TaskDTO::getId))
                .toList();
    }

    @Override
    public TaskDTO findById(String id) {
        Integer taskId = TryParse.Int(id);
        return taskRepository.findById(taskId)
                .map(TaskMapper::toDto)
                .orElseThrow(TaskNotFoundException::new);
    }

    @Override
    @Transactional
    public void update(TaskDTO taskDTO) {
        Task task = TaskMapper.toEntity(taskDTO);
        Task taskUpdated = fillNullFields(task);
        taskRepository.update(taskUpdated);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        Integer taskId = TryParse.Int(id);
        if (!taskRepository.existsById(taskId)) {
            throw new TaskNotFoundException();
        } else {
            taskRepository.deleteById(taskId);
        }
    }

    @Override
    public List<EmployeeDTO> findEmployeesByTaskId(String id) {
        Integer taskId = TryParse.Int(id);
        if (!taskRepository.existsById(taskId)) {
            throw new TaskNotFoundException();
        } else {
            return taskRepository.findEmployeesByTaskId(taskId).stream()
                    .map(EmployeeMapper::toDto)
                    .sorted(Comparator.comparing(EmployeeDTO::getId))
                    .toList();
        }
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
}
