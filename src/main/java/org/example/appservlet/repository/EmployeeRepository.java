package org.example.appservlet.repository;

import org.example.appservlet.model.Employee;
import org.example.appservlet.model.Task;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    @EntityGraph(attributePaths = {"department"})
    List<Employee> findAll();

    @Modifying
    @Query("UPDATE Employee e SET e.firstname = :#{#employee.firstname}, e.lastname = :#{#employee.lastname}, e.email = :#{#employee.email}, e.age = :#{#employee.age} WHERE e.id = :#{employee.id}")
    Employee update(@Param("employee") Employee employee);

    @Query("SELECT t FROM Task t JOIN t.employees e WHERE e.id = :employeeId")
    List<Task> findTasksByEmployeeId(@Param("employeeId") Integer employeeId);
}
