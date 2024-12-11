package org.example.appservlet.exception;

public class EmployeeNotFoundException extends RuntimeException {
    private static final String EMPLOYEE_NOT_FOUND = "Сотрудник с данным ID не найден!";

    public EmployeeNotFoundException() {
        super(EMPLOYEE_NOT_FOUND);
    }
}
