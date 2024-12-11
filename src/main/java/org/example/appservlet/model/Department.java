package org.example.appservlet.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Department {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "department_id_seq")
    @SequenceGenerator(name = "department_id_seq", sequenceName = "department_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "department_name")
    private String departmentName;

    @Column(name = "location")
    private String location;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "department", cascade = CascadeType.ALL)
    private Set<Employee> employees = new HashSet<>(0);
}