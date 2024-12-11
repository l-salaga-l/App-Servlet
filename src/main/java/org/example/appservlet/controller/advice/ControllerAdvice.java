package org.example.appservlet.controller.advice;

import org.example.appservlet.exception.BadRequestException;
import org.example.appservlet.dto.Response;

import org.example.appservlet.exception.DepartmentNotFoundException;
import org.example.appservlet.exception.EmployeeNotFoundException;
import org.example.appservlet.exception.TaskNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Response> handleBadRequest(Exception e) {
        Response errorResponse = new Response(e.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse);
    }

    @ExceptionHandler({DepartmentNotFoundException.class, EmployeeNotFoundException.class,
            TaskNotFoundException.class})
    public ResponseEntity<Response> handleNotFoundException(DepartmentNotFoundException e) {
        Response errorResponse = new Response(e.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse);
    }
}
