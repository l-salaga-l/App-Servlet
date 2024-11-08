package org.example.appservlet.model;

import java.util.HashSet;
import java.util.Set;

/**
 *  Сущность "Задания"<br>
 *  Отношения:<br>
 *  Many to Many: Task <-> Employee
 */
public class Task {
    private Integer id;
    private String taskName;
    private String deadline;
    private Set<Employee> employees;

    public Task() {}

    public Task(Integer id, String taskName, String deadline, Set<Employee> employees) {
        this.id = id;
        this.taskName = taskName;
        this.deadline = deadline;
        this.employees = employees;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public Set<Employee> getEmployees() {
        if (employees == null) {
            employees = new HashSet<Employee>();
        }
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }
}
