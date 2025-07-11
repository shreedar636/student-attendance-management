package com.studentattendance.dtos;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateTeacherRequest {
    private String firstName;
    private String lastName;
    @Email
    private String email;
    private String password;
}
