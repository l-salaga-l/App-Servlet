package org.example.appservlet.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequestDto {
    private String firstname;
    private String lastname;
    private String email;
    private int age;
    private int departmentId;
}
