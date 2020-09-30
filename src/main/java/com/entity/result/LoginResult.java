package com.entity.result;

import com.entity.User;

public class LoginResult extends Result {
    private boolean isLogin;

    protected LoginResult(String status, String msg, boolean isLogin, User user) {
        super(status, msg, user);
        this.isLogin = isLogin;
    }

    public static Result success(String message, User user) {
        return new LoginResult("ok", message, true, user);

    }

    public static Result failure(String status, String message) {
        return new LoginResult(status, message, false, null);
    }

    public boolean isLogin() {
        return isLogin;
    }
}
