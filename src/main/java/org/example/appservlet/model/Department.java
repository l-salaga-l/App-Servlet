package org.example.appservlet.model;

import java.util.HashSet;
import java.util.Set;

/**
 *  Сущность "Отдел"<br>
 *  Отношения:<br>
 *  One to Many: Department <- Employee
 */
public class Department {
    private Integer id;
    private String departmentName;
    private String location;
    private Set<Employee> employees;

    public Department() {}

    public Department(Integer id, String departmentName, String location, Set<Employee> employees) {
        this.id = id;
        this.departmentName = departmentName;
        this.location = location;
        this.employees = employees;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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