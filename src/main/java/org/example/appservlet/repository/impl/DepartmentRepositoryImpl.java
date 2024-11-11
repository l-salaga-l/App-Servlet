package org.example.appservlet.repository.impl;

import org.example.appservlet.db.ConnectionManager;
import org.example.appservlet.db.ConnectionManagerImpl;
import org.example.appservlet.model.Department;
import org.example.appservlet.model.Employee;
import org.example.appservlet.repository.DepartmentRepository;
import org.example.appservlet.util.SetFromIterable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class DepartmentRepositoryImpl implements DepartmentRepository {
    private static final ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();

    public DepartmentRepositoryImpl() {}

    @Override
    public Department save(Department department) {
        String INSERT_VALUE = """
                                INSERT INTO department (department_name, location)
                                VALUES (?, ?)""";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_VALUE, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, department.getDepartmentName());
            preparedStatement.setString(2, department.getLocation());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                department = createDepartment(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return department;
    }

    @Override
    public void deleteById(Integer id) {
        String DELETE_BY_ID = "DELETE FROM department WHERE id = ?";

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
        String EXIST = "SELECT EXISTS(SELECT 1 FROM department WHERE id = ?)";

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
    public void update(Department department) {
        String UPDATE_VALUE = """
                                UPDATE department
                                SET department_name = ?, location = ?
                                WHERE id = ?""";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_VALUE)) {

            preparedStatement.setString(1, department.getDepartmentName());
            preparedStatement.setString(2, department.getLocation());
            preparedStatement.setInt(3, department.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Department> findById(Integer id) {
        String FIND_BY_ID = "SELECT * FROM department WHERE id = ?";

        Department department = null;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                department = createDepartment(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(department);
    }

    @Override
    public Iterable<Department> findAll() {
        String FIND_ALL = "SELECT * FROM department";

        List<Department> departments = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                departments.add(createDepartment(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return departments;
    }

    @Override
    public Iterable<Employee> findEmployeesByDepartmentId(Integer departmentId) {
        String FIND_EMPLOYEES_BY_DEPARTMENT_ID = """
                                            SELECT *
                                            FROM employee e
                                            WHERE e.department_id = ?""";

        List<Employee> employees = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_EMPLOYEES_BY_DEPARTMENT_ID)) {

            preparedStatement.setInt(1, departmentId);
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

    private Department createDepartment(ResultSet resultSet) throws SQLException {
        Set<Employee> employees = SetFromIterable.toSet(findEmployeesByDepartmentId(resultSet.getInt("id")));

        return new Department(
                resultSet.getInt("id"),
                resultSet.getString("department_name"),
                resultSet.getString("location"),
                employees
        );
    }
}
