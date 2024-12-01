package org.example.appservlet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Response {
    private String date;
    private int status;
    private String message;

    public Response(String message, HttpStatus status) {
        this.date = LocalDateTime.now().toString();
        this.status = status.value();
        this.message = message;
    }
}
