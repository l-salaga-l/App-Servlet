package org.example.appservlet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 *  Сущность "Отдел"<br>
 *  Отношения:<br>
 *  One to Many: Department <- Employee
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Integer id;

    @Column(name = "department_name")
    private String departmentName;

    @Column(name = "location")
    private String location;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "department", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Employee> employees = new HashSet<>(0);

    public boolean addEmployee(Employee employee) {
        return employees.add(employee);
    }

    public boolean removeEmployee(Employee employee) {
        return employees.remove(employee);
    }
}