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
public interface TaskRepository extends JpaRepository<Task, Integer> {
    @EntityGraph(attributePaths = {"employees"})
    List<Task> findAll();

    @Query("SELECT e FROM Employee e JOIN e.tasks t WHERE t.id = :taskId")
    List<Employee> findEmployeesByTaskId(@Param("taskId") Integer taskId);

    @Modifying
    @Query("UPDATE Task t SET t.taskName = :#{#task.taskName}, t.deadline = :#{#task.deadline} WHERE t.id = :#{#task.id}")
    void update(@Param("task") Task task);
}
