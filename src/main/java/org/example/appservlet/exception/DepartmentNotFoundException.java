package org.example.appservlet.exception;

public class DepartmentNotFoundException extends RuntimeException {
    private static final String DEPARTMENT_NOT_FOUND = "Отдел с данным ID не найден!";

    public DepartmentNotFoundException() {
        super(DEPARTMENT_NOT_FOUND);
    }
}
