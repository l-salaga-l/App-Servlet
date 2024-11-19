package org.example.appservlet.service.impl;

import org.example.appservlet.exception.NotFoundException;
import org.example.appservlet.model.Employee;
import org.example.appservlet.model.Task;
import org.example.appservlet.repository.TaskRepository;
import org.example.appservlet.service.TaskService;

public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Iterable<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public Task findById(Integer id) throws NotFoundException {
        return taskRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public void update(Task task) throws NotFoundException {
        try {
            checkExist(task.getId());
            taskRepository.update(fillNullFields(task));
        } catch (NotFoundException e) {
            throw new NotFoundException();
        }
    }

    @Override
    public void deleteById(Integer id) throws NotFoundException {
        try {
            checkExist(id);
            taskRepository.deleteById(id);
        } catch (NotFoundException e) {
            throw new NotFoundException();
        }
    }

    @Override
    public Iterable<Employee> findEmployeesByTaskId(Integer taskId) throws NotFoundException {
        try {
            checkExist(taskId);
        } catch (NotFoundException e) {
            throw new NotFoundException();
        }
        return taskRepository.findEmployeesByTaskId(taskId);
    }

    private void checkExist(Integer id) throws NotFoundException {
        if (!taskRepository.existsById(id)) {
            throw new NotFoundException();
        }
    }

    private Task fillNullFields(Task task_new) throws NotFoundException {
        Task task_old = findById(task_new.getId());

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
