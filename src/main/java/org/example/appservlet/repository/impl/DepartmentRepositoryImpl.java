package org.example.appservlet.repository.impl;

import jakarta.persistence.criteria.*;

import org.example.appservlet.db.HibernateUtil;
import org.example.appservlet.db.TransactionOperation;
import org.example.appservlet.model.Department;
import org.example.appservlet.model.Employee;
import org.example.appservlet.repository.DepartmentRepository;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Optional;

public class DepartmentRepositoryImpl implements DepartmentRepository {

    @Override
    public Department save(Department department) {
        return executeTransaction(session -> {
           session.persist(department);
           return department;
        });
    }

    @Override
    public void deleteById(Integer id) throws ObjectNotFoundException {
        executeTransaction(session -> {
           Department department = session.get(Department.class, id);
           if (department == null) {
               throw new ObjectNotFoundException(id, "Нет записи с данным идентификатором!");
           }
           session.remove(department);
           return null;
        });
    }

    @Override
    public void update(Department department) {
        executeTransaction(session -> {
           session.merge(department);
           return null;
        });
    }

    @Override
    public Optional<Department> findById(Integer id) {
        return executeTransaction(session -> {
           Department department = session.get(Department.class, id);
           if (department == null) {
               throw new ObjectNotFoundException(id, "Нет записи с данным идентификатором!");
           }
           return Optional.of(department);
        });
    }

    @Override
    public Iterable<Department> findAll() {
        return executeTransaction(session -> {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Department> criteriaQuery = criteriaBuilder.createQuery(Department.class);
            Root<Department> departmentRoot = criteriaQuery.from(Department.class);
            departmentRoot.fetch("employees", JoinType.LEFT);
            criteriaQuery.select(departmentRoot);
            return session.createQuery(criteriaQuery).getResultList();
        });
    }

    @Override
    public boolean existsById(Integer id) {
        return executeTransaction(session -> {
           Department department = session.get(Department.class, id);
           return department != null;
        });
    }

    @Override
    public Iterable<Employee> findEmployeesByDepartmentId(Integer departmentId) {
        return executeTransaction(session -> {
           CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
           CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
           Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);
           employeeRoot.fetch("department", JoinType.LEFT);
           employeeRoot.fetch("tasks", JoinType.LEFT);
           Predicate departmentPredicate = criteriaBuilder.equal(employeeRoot.get("department").get("id"), departmentId);
           criteriaQuery.select(employeeRoot).where(departmentPredicate);
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
