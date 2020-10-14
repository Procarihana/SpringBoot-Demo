package com.controller;


import com.entity.result.Result;
import com.entity.User;
import com.entity.result.LoginResult;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

import java.util.Map;

@Controller
public class AutoController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;

    @Inject
    public AutoController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }


    @GetMapping(value = "/auth", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object auth(ModelMap map) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userService.getUserByUsername(authentication == null ?
            null : authentication.getName());

        if (loggedInUser == null) {
            return LoginResult.failure("ok", "用户没有登录");
        } else {
            return LoginResult.success(null, loggedInUser);
        }
    }

    @PostMapping(value = "/auth/login", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object login(@RequestBody Map<String, Object> usernameAndPassword) {
        String username = usernameAndPassword.get("username").toString();
        String password = usernameAndPassword.get("password").toString();
        UserDetails userDetails;
        try {
            userDetails = userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return LoginResult.failure("fail", "用户不存在");
        }
        try {
            login(password, userDetails);
            return LoginResult.success("登录成功", userService.getUserByUsername(username));
        } catch (BadCredentialsException e) {
            return LoginResult.failure("fail", "密码不正确");
        }

    }

    @GetMapping(value = "/auth/logout", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userService.getUserByUsername(authentication == null ? null : authentication.getName());

        if (loggedInUser == null) {
            return LoginResult.failure("fail", "用户尚未登录");
        } else {
            SecurityContextHolder.clearContext();
            return LoginResult.failure("ok", "注销成功");
        }
    }

    @PostMapping(value = "/auth/register", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result register(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        UserDetails userDetails;
        if (username == null || password == null) {
            return LoginResult.failure("fail", "错误原因：注册用户没有输入名字或密码");
        }
        if (username.length() < 1 || username.length() > 15) {
            return LoginResult.failure("fail", "错误原因：用户名字不符合'长度1到15个字符，只能是字母数字下划线中'的规定");
        }
        if (password.length() < 1 || password.length() > 15) {
            return LoginResult.failure("fail", "错误原因：用户密码不符合'长度6到16个任意字符'的规定");
        }
        try {
            userService.save(username, password);
            //这里在并发的过程中，如果有人同时申请两个同样的用户名字，就会出现数据库重复的情况，可以通过数据库约束完成事务
            User loggedInUser = userService.getUserByUsername(username);
            userDetails = userService.loadUserByUsername(username);
            login(password, userDetails);
            return LoginResult.success("注册成功", loggedInUser);
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            return LoginResult.failure("fail", "错误原因:用户已存在");
        }
    }

    private void login(String password, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken
            (userDetails, password, userDetails.getAuthorities());
        authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(token);
    }
}
