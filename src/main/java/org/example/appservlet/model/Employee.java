package org.example.appservlet.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Сущность "Сотрудник"<br>
 * Отношения:<br>
 * Many to One: Employee -> Department<br>
 * Many to Many: Employee <-> Task
 */
public class Employee {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private int age;
    private Department department;
    private Set<Task> tasks;

    public Employee() {}

    public Employee(Integer id, String firstname, String lastname, String email, int age, Department department, Set<Task> tasks) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.age = age;
        this.department = department;
        this.tasks = tasks;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Set<Task> getTasks() {
        if (tasks == null) {
            tasks = new HashSet<Task>();
        }
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }
}
