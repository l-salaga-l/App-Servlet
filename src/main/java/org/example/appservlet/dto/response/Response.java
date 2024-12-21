package org.example.appservlet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Response {
    private String date;
    private String message;

    public Response(String message) {
        this.date = LocalDateTime.now().toString();
        this.message = message;
    }
}
