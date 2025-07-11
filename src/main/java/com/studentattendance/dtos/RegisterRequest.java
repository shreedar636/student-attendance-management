package com.studentattendance.dtos;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RegisterRequest {
    private String firstName;
    private String lastName;
    @Email
    private String email;
    private String password;
    private Set<String> roles;
}