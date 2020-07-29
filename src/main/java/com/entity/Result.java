package com.entity;

public class Result {
    String status;
    String msg;
    boolean isLogin;
    Object Data;

    public static Result failure(String message) {
        return new Result("fail", message);
    }

    public static Result success(String message, Object data) {
        return new Result("ok", message, data);
    }


    //通过静态工厂方法重构登录为`fail`、`success`的代码
    public Result(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public Result(String status, boolean isLogin) {
        this.status = status;
        this.isLogin = isLogin;
    }

    public Result(String status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        Data = data;
    }

    public Result(String status, boolean isLogin, Object data) {
        this.status = status;
        this.isLogin = isLogin;
        Data = data;
    }   //通过静态工厂方法按需完成构造器，这个构造器就不被需要

    private Result(String status, String msg, boolean isLogin, Object data) {
        this.status = status;
        this.msg = msg;
        this.isLogin = isLogin;
        Data = data;
    }

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public Object getData() {
        return Data;
    }
}
