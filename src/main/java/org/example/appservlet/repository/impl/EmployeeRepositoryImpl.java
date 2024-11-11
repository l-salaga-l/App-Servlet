package org.example.appservlet.repository.impl;

import org.example.appservlet.db.ConnectionManager;
import org.example.appservlet.db.ConnectionManagerImpl;
import org.example.appservlet.model.Department;
import org.example.appservlet.model.Employee;
import org.example.appservlet.model.Task;
import org.example.appservlet.repository.EmployeeRepository;
import org.example.appservlet.util.SetFromIterable;

import java.sql.*;
import java.util.*;

public class EmployeeRepositoryImpl implements EmployeeRepository {
    private static final ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();

    public EmployeeRepositoryImpl() {}

    @Override
    public Employee save(Employee employee) {
        String INSERT_VALUE = """
                                INSERT INTO employee (firstname, lastname, email, age, department_id)
                                VALUES (?, ?, ?, ?, ?)""";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_VALUE, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, employee.getFirstname());
            preparedStatement.setString(2, employee.getLastname());
            preparedStatement.setString(3, employee.getEmail());
            preparedStatement.setInt(4, employee.getAge());
            preparedStatement.setInt(5, employee.getDepartment().getId());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                employee = createEmployee(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return employee;
    }

    @Override
    public void deleteById(Integer id) {
        String DELETE_BY_ID = "DELETE FROM employee WHERE id = ?";

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
        String EXIST = "SELECT EXISTS(SELECT 1 FROM employee WHERE id = ?)";

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
    public void update(Employee employee) {
        String UPDATE_VALUE = """
                                UPDATE employee
                                SET firstname = ?, lastname = ?, email = ?, age = ?, department_id = ?
                                WHERE id = ?""";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_VALUE)) {

            preparedStatement.setString(1, employee.getFirstname());
            preparedStatement.setString(2, employee.getLastname());
            preparedStatement.setString(3, employee.getEmail());
            preparedStatement.setInt(4, employee.getAge());
            preparedStatement.setInt(5, employee.getDepartment().getId());
            preparedStatement.setInt(6, employee.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Employee> findById(Integer id) {
        String FIND_BY_ID = "SELECT * FROM employee WHERE id = ?";

        Employee employee = null;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                employee = createEmployee(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(employee);
    }

    @Override
    public Iterable<Employee> findAll() {
        String FIND_ALL = "SELECT * FROM employee";

        List<Employee> employees = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                employees.add(createEmployee(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return employees;
    }

    @Override
    public Optional<Department> findDepartmentByEmployeeId(Integer employeeId) {
        String FIND_DEPARTMENT_BY_EMPLOYEE_ID = """
                                                SELECT * FROM department d
                                                JOIN employee e ON d.id = e.department_id
                                                WHERE e.id = ?""";

        Department department = null;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_DEPARTMENT_BY_EMPLOYEE_ID)) {

            preparedStatement.setInt(1, employeeId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                department = new Department(
                        resultSet.getInt("id"),
                        resultSet.getString("department_name"),
                        resultSet.getString("location"),
                        null
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(department);
    }

    @Override
    public Iterable<Task> findTasksByEmployeeId(Integer employeeId) {
        String FIND_TASKS_BY_EMPLOYEE_ID = """
                                            SELECT t.id, t.task_name, t.deadline
                                            FROM task t
                                            INNER JOIN assignments a ON t.id = a.task_id
                                            WHERE a.employee_id = ?""";

        List<Task> tasks = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_TASKS_BY_EMPLOYEE_ID)) {

            preparedStatement.setInt(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                tasks.add(new Task(
                         resultSet.getInt("id"),
                         resultSet.getString("task_name"),
                         resultSet.getDate("deadline").toString(),
                        null));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tasks;
    }

    private Employee createEmployee(ResultSet resultSet) throws SQLException {
        Department department = findDepartmentByEmployeeId(resultSet.getInt("id")).orElse(null);
        Set<Task> tasks = SetFromIterable.toSet(findTasksByEmployeeId(resultSet.getInt("id")));

        return new Employee(
                resultSet.getInt("id"),
                resultSet.getString("firstname"),
                resultSet.getString("lastname"),
                resultSet.getString("email"),
                resultSet.getInt("age"),
                department,
                tasks
        );
    }
}
