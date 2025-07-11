package com.studentattendance.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private Integer code;
    private String message;
    private String token;

    public LoginResponse(Integer code, String message, String token) {
        this.code = code;
        this.message = message;
        this.token = token;
    }
}