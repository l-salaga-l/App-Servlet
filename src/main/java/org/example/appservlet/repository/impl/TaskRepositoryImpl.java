package org.example.appservlet.repository.impl;

import org.example.appservlet.db.ConnectionManager;
import org.example.appservlet.db.ConnectionManagerImpl;
import org.example.appservlet.model.Employee;
import org.example.appservlet.model.Task;
import org.example.appservlet.repository.TaskRepository;
import org.example.appservlet.util.SetFromIterable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class TaskRepositoryImpl implements TaskRepository {
    private static final ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();

    public TaskRepositoryImpl() {}

    @Override
    public Task save(Task task) {
        String INSERT_VALUE = """
                                INSERT INTO task (task_name, deadline)
                                VALUES (?, ?)""";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_VALUE, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, task.getTaskName());
            preparedStatement.setString(2, task.getDeadline());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                task = createTask(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return task;
    }

    @Override
    public void deleteById(Integer id) {
        String DELETE_BY_ID = "DELETE FROM task WHERE id = ?";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsById(Integer id) {
        String EXIST = "SELECT EXISTS(SELECT 1 FROM task WHERE id = ?)";

        boolean isExist = false;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EXIST)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                isExist = resultSet.getBoolean(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return isExist;
    }

    @Override
    public void update(Task task) {
        String UPDATE_VALUE = """
                                UPDATE task
                                SET task_name = ?, deadline = ?
                                WHERE id = ?""";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_VALUE)) {

            preparedStatement.setString(1, task.getTaskName());
            preparedStatement.setString(2, task.getDeadline());
            preparedStatement.setInt(3, task.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Task> findById(Integer id) {
        String FIND_BY_ID = "SELECT * FROM task WHERE id = ?";

        Task task = null;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                task = createTask(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(task);
    }

    @Override
    public Iterable<Task> findAll() {
        String FIND_ALL = "SELECT * FROM task";

        List<Task> tasks = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                tasks.add(createTask(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tasks;
    }

    @Override
    public Iterable<Employee> findEmployeesByTaskId(Integer taskId) {
        String FIND_TASKS_BY_EMPLOYEE_ID = """
                                            SELECT *
                                            FROM employee e
                                            INNER JOIN assignments a ON e.id = a.employee_id
                                            WHERE a.task_id = ?""";

        List<Employee> employees = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_TASKS_BY_EMPLOYEE_ID)) {

            preparedStatement.setInt(1, taskId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                employees.add(new Employee(
                        resultSet.getInt("id"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getString("email"),
                        resultSet.getInt("age"),
                        null,
                        null
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return employees;
    }

    private Task createTask(ResultSet resultSet) throws SQLException {
        Set<Employee> employees = SetFromIterable.toSet(findEmployeesByTaskId(resultSet.getInt("id")));

        return new Task(
                resultSet.getInt("id"),
                resultSet.getString("task_name"),
                resultSet.getString("deadline"),
                employees
        );
    }
}
