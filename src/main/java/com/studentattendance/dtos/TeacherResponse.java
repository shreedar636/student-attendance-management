package com.studentattendance.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherResponse {
    private Integer code;
    private String message;
    public TeacherResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
