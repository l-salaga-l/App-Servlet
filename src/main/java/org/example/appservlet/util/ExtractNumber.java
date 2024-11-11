package org.example.appservlet.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractNumber {
    public static Integer apply(String input) throws NumberFormatException {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return Integer.valueOf(matcher.group());
        } else {
            throw new NumberFormatException();
        }
    }
}
