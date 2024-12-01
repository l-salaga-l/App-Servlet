package org.example.appservlet.util;

import org.example.appservlet.exception.BadRequestException;

public class TryParse {
    public static Integer Int(String input) {
        int id;
        try {
            id = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new BadRequestException("id is invalid");
        }
        return id;
    }
}
