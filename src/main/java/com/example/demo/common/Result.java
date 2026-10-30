package com.example.demo.common;

public class Result<T> {
    Integer code;
    String message;
    T data;

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() { return code; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public void setCode(Integer code) { this.code = code; }
    public void setMessage(String message) { this.message = message; }
    public void setData(T data) { this.data = data; }

    // 默认成功
    static <T> Result<T> success(T data) { return new Result<>(200, "操作成功", data); }

    // 默认失败
    static <T> Result<T> fail(T data) { return new Result<>(300, "操作失败", data); }

    // 自定义成功消息
    static <T> Result<T> success( String message, T data) { return new Result<>(200, message, data); }

    // 自定义失败消息
    static <T> Result<T> fail( String message, T data) { return new Result<>(300, message, data); }
}