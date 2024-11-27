package org.example.appservlet.service.impl;

import org.example.appservlet.model.Employee;
import org.example.appservlet.model.Task;
import org.example.appservlet.repository.TaskRepository;
import org.example.appservlet.service.TaskService;
import org.example.appservlet.service.dto.EmployeeDTO;
import org.example.appservlet.service.dto.TaskDTO;
import org.example.appservlet.service.mapper.EmployeeMapper;
import org.example.appservlet.service.mapper.TaskMapper;

import org.hibernate.ObjectNotFoundException;

public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskDTO save(TaskDTO taskDTO) {
        Task task = TaskMapper.toEntity(taskDTO);
        Task savedTask = taskRepository.save(task);
        return TaskMapper.toDto(savedTask);
    }

    @Override
    public Iterable<TaskDTO> findAll() {
        Iterable<Task> tasks = taskRepository.findAll();
        return TaskMapper.toDto(tasks);
    }

    @Override
    public TaskDTO findById(Integer id) throws ObjectNotFoundException {
        Task task = taskRepository.findById(id).orElseThrow();
        return TaskMapper.toDto(task);
    }

    @Override
    public void update(TaskDTO taskDTO) throws ObjectNotFoundException {
        Task task = TaskMapper.toEntity(taskDTO);
        Task taskUpdated = fillNullFields(task);
        taskRepository.update(taskUpdated);
    }

    @Override
    public void deleteById(Integer id) throws ObjectNotFoundException {
        taskRepository.deleteById(id);
    }

    @Override
    public Iterable<EmployeeDTO> findEmployeesByTaskId(Integer taskId) throws ObjectNotFoundException {
        Iterable<Employee> employees = taskRepository.findEmployeesByTaskId(taskId);
        return EmployeeMapper.toDto(employees);
    }

    private Task fillNullFields(Task task_new) throws ObjectNotFoundException {
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
