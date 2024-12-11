package org.example.appservlet.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 *  Сущность "Задания"<br>
 *  Отношения:<br>
 *  Many to Many: Task <-> Employee
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Task {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_id_seq")
    @SequenceGenerator(name = "task_id_seq", sequenceName = "task_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "deadline")
    private String deadline;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "assignments",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id"))
    private Set<Employee> employees = new HashSet<>(0);
}
