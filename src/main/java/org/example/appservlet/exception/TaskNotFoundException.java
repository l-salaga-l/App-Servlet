package org.example.appservlet.exception;

public class TaskNotFoundException extends RuntimeException {
    private static final String TASK_NOT_FOUND = "Задача с данным ID не найден!";

    public TaskNotFoundException() {
        super(TASK_NOT_FOUND);
    }
}
