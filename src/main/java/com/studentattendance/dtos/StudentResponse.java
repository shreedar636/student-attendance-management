package com.studentattendance.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentResponse {
    private Integer code;
    private String message;
    public StudentResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
