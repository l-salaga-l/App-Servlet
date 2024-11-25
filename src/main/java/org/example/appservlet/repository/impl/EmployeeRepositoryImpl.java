package org.example.appservlet.repository.impl;

import jakarta.persistence.criteria.*;

import org.example.appservlet.db.HibernateUtil;
import org.example.appservlet.model.Employee;
import org.example.appservlet.model.Task;
import org.example.appservlet.repository.EmployeeRepository;
import org.example.appservlet.db.TransactionOperation;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.*;

public class EmployeeRepositoryImpl implements EmployeeRepository {

    /**
     * Сохраняет нового сотрудника в базе данных.
     *
     * @param employee сотрудник для сохранения
     * @return сохраненный сотрудник
     */
    @Override
    public Employee save(Employee employee) {
        return executeTransaction(session -> {
            session.persist(employee);
            return employee;
        });
    }

    /**
     * Удаляет сотрудника по его идентификатору.
     *
     * @param id идентификатор сотрудника для удаления
     * @throws ObjectNotFoundException если сотрудник с данным идентификатором не найден
     */
    @Override
    public void deleteById(Integer id) throws ObjectNotFoundException {
        executeTransaction(session -> {
            Employee employee = session.get(Employee.class, id);
            if (employee == null) {
                throw new ObjectNotFoundException(id, "Нет записи с данным идентификатором!");
            }
            session.remove(employee);
            return null;
        });
    }

    /**
     * Обновляет существующего сотрудника в базе данных.
     *
     * @param employee сотрудник для обновления
     */
    @Override
    public void update(Employee employee) {
        executeTransaction(session -> {
            session.merge(employee);
            return null;
        });
    }

    /**
     * Находит сотрудника по его идентификатору.
     *
     * @param id идентификатор сотрудника для поиска
     * @return Optional, содержащий найденного сотрудника, или пустой, если не найден
     * @throws ObjectNotFoundException если сотрудник с данным идентификатором не найден
     */
    @Override
    public Optional<Employee> findById(Integer id) throws ObjectNotFoundException {
        return executeTransaction(session -> {
            Employee employee = session.get(Employee.class, id);
            if (employee == null) {
                throw new ObjectNotFoundException(id, "Нет записи с данным идентификатором!");
            }

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
            Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);
            employeeRoot.fetch("tasks", JoinType.LEFT);
            criteriaQuery.select(employeeRoot).where(criteriaBuilder.equal(employeeRoot.get("id"), id));

            return Optional.ofNullable(session.createQuery(criteriaQuery).uniqueResult());
        });
    }

    /**
     * Находит всех сотрудников в базе данных.
     *
     * @return Iterable всех сотрудников
     */
    @Override
    public Iterable<Employee> findAll() {
        return executeTransaction(session -> {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
            Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);
            employeeRoot.fetch("department", JoinType.LEFT);
            employeeRoot.fetch("tasks", JoinType.LEFT);
            criteriaQuery.select(employeeRoot);
            return session.createQuery(criteriaQuery).getResultList();
        });
    }

    /**
     * Проверяет, существует ли сотрудник по его идентификатору.
     *
     * @param id идентификатор сотрудника для проверки
     * @return true, если сотрудник существует, иначе false
     */
    @Override
    public boolean existsById(Integer id) {
        return executeTransaction(session -> {
            Employee employee = session.get(Employee.class, id);
            return employee != null;
        });
    }

    /**
     * Находит все задачи, назначенные конкретному сотруднику по его идентификатору.
     *
     * @param employeeId идентификатор сотрудника
     * @return Iterable задач, назначенных сотруднику
     */
    @Override
    public Iterable<Task> findTasksByEmployeeId(Integer employeeId) {
        return executeTransaction(session -> {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Task> criteriaQuery = criteriaBuilder.createQuery(Task.class);
            Root<Task> taskRoot = criteriaQuery.from(Task.class);
            Join<Task, Employee> assignmentsJoin = taskRoot.join("employees");
            Predicate employeePredicate = criteriaBuilder.equal(assignmentsJoin.get("id"), employeeId);
            criteriaQuery.select(taskRoot).where(employeePredicate);
            return session.createQuery(criteriaQuery).getResultList();
        });
    }

    /**
     * Выполняет операцию с базой данных в рамках транзакции.
     *
     * @param operation операция для выполнения
     * @param <T> тип результата
     * @return результат операции
     */
    private <T> T executeTransaction(TransactionOperation<T> operation) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            T result = operation.execute(session);
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Ошибка при выполнении операции в транзакции!\n" + e.getMessage());
        }
    }
}