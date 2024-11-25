package org.example.appservlet.repository.impl;

import jakarta.persistence.criteria.*;

import org.example.appservlet.db.HibernateUtil;
import org.example.appservlet.db.TransactionOperation;
import org.example.appservlet.model.Employee;
import org.example.appservlet.model.Task;
import org.example.appservlet.repository.TaskRepository;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Optional;

public class TaskRepositoryImpl implements TaskRepository {

    /**
     * Сохраняет новую задачу в базе данных.
     *
     * @param task задача для сохранения
     * @return сохраненная задача
     */
    @Override
    public Task save(Task task) {
        return executeTransaction(session -> {
           session.persist(task);
           return task;
        });
    }

    /**
     * Удаляет задачу по его идентификатору.
     *
     * @param id идентификатор задачи для удаления
     * @throws ObjectNotFoundException если задачи с данным идентификатором не найден
     */
    @Override
    public void deleteById(Integer id) {
        executeTransaction(session -> {
           Task task = session.get(Task.class, id);
           if (task == null) {
               throw new ObjectNotFoundException(id, "Нет записи с данным идентификатором!");
           }
           session.remove(task);
           return null;
        });
    }

    /**
     * Обновляет существующее задачу в базе данных.
     *
     * @param task задача для обновления
     */
    @Override
    public void update(Task task) {
        executeTransaction(session -> {
            session.merge(task);
            return null;
        });
    }

    /**
     * Находит задачу по идентификатору.
     *
     * @param id идентификатор задачи для поиска
     * @return Optional, содержащий найденную задачу, или пустой, если не найден
     * @throws ObjectNotFoundException если задачи с данным идентификатором не найден
     */
    @Override
    public Optional<Task> findById(Integer id) throws ObjectNotFoundException {
        return executeTransaction(session -> {
           Task task = session.get(Task.class, id);
           if (task == null) {
               throw new ObjectNotFoundException(id, "");
           }

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Task> criteriaQuery = criteriaBuilder.createQuery(Task.class);
            Root<Task> taskRoot = criteriaQuery.from(Task.class);
            taskRoot.fetch("employees", JoinType.LEFT);
            criteriaQuery.select(taskRoot).where(criteriaBuilder.equal(taskRoot.get("id"), id));

            return Optional.ofNullable(session.createQuery(criteriaQuery).uniqueResult());
        });
    }

    /**
     * Находит все задачи в базе данных.
     *
     * @return Iterable всех задач
     */
    @Override
    public Iterable<Task> findAll() {
        return executeTransaction(session -> {
           CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
           CriteriaQuery<Task> criteriaQuery = criteriaBuilder.createQuery(Task.class);
           Root<Task> taskRoot = criteriaQuery.from(Task.class);
           taskRoot.fetch("employees", JoinType.LEFT);
           criteriaQuery.select(taskRoot);
           return session.createQuery(criteriaQuery).getResultList();
        });
    }

    /**
     * Проверяет, существует ли задача по заданному идентификатору.
     *
     * @param id идентификатор задачи для проверки
     * @return true, если задача существует, иначе false
     */
    @Override
    public boolean existsById(Integer id) {
        return executeTransaction(session -> {
           Task task = session.get(Task.class, id);
           return task != null;
        });
    }

    /**
     * Находит всех сотрудников, закрепленные за заданной задачей
     *
     * @param taskId идентификатор задачи
     * @return Iterable сотрудников, закрепленные за задачей
     */
    @Override
    public Iterable<Employee> findEmployeesByTaskId(Integer taskId) {
        return executeTransaction(session -> {
           CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
           CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
           Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);
           Join<Employee, Task> assignmentsJoin = employeeRoot.join("tasks", JoinType.LEFT);
           Predicate taskPredicate = criteriaBuilder.equal(assignmentsJoin.get("id"), taskId);
           criteriaQuery.select(employeeRoot).where(taskPredicate);
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
