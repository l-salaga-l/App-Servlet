package org.example.appservlet.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Сущность "Сотрудник"<br>
 * Отношения:<br>
 * Many to One: Employee -> Department<br>
 * Many to Many: Employee <-> Task
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Employee {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Integer id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "age")
    private int age;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "employees")
    private Set<Task> tasks = new HashSet<>(0);

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }
}
