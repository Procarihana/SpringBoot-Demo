package com.entity;

public abstract class Result<T> {
    String status;
    String msg;
    T data;

//    public static Result failure(String message) {
//        return new Result("fail", message);
//    }
//
//    public static Result success(String message, Object data) {
//        return new Result("ok", message, data);
//    }


    //通过静态工厂方法重构登录为`fail`、`success`的代码
    protected Result(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    protected Result(String status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
    //通过静态工厂方法按需完成构造器，这个构造器就不被需要

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}
