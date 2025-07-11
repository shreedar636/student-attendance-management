package com.studentattendance.errorhandler;

public class ServiceResult<T> {
    private boolean success;
    private T data;
    private String errorCode;

    public ServiceResult(boolean success, T data, String errorCode) {
        this.success = success;
        this.data = data;
        this.errorCode = errorCode;
    }

    public static <T> ServiceResult<T> success(T data) {
        return new ServiceResult<>(true, data, null);
    }

    public static <T> ServiceResult<T> error(T data, String errorCode) {
        return new ServiceResult<>(false, data, errorCode);
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getErrorCode() {
        return errorCode;
    }
}