package org.example.appservlet.repository;

import org.example.appservlet.model.Department;
import org.example.appservlet.model.Employee;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    @EntityGraph(attributePaths = {"employees"})
    List<Department> findAll();

    @Modifying
    @Query("UPDATE Department d SET d.departmentName = :#{#department.departmentName}, d.location = :#{#department.location} WHERE d.id = :#{#department.id}")
    Department update(@Param("department") Department department);


    @Query("SELECT e FROM Employee e WHERE e.department.id = :departmentId")
    List<Employee> findEmployeesByDepartmentId(@Param("departmentId") Integer departmentId);
}
