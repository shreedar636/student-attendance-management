package com.studentattendance.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterResponse {
    private Integer code;
    private String message;

    public RegisterResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}