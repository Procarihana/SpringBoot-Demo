package com.controller;


import com.entity.Result;
import com.entity.User;
import com.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    private AuthenticationManager authenticationManager;//管理鉴权
    private UserService userService;

    @Inject
    public AutoController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }


    @PostMapping("/auth/register")
    @ResponseBody
    public Result register(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        if (username == null || password == null) {
            return Result.failure("错误原因：注册用户没有输入名字或密码");
        }
        if (username.length() < 1 || username.length() > 15) {
            return Result.failure("错误原因：用户名字不符合'长度1到15个字符，只能是字母数字下划线中'的规定");
        }
        if (password.length() < 1 || password.length() > 15) {
            return Result.failure("错误原因：用户密码不符合'长度6到16个任意字符'的规定");
        }
        try {
            userService.save(username, password);
            User loggedInUser = userService.getUserByUsername(username);
            return Result.success("注册成功", loggedInUser);
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            return Result.failure("错误原因:用户已存在");
        }
//        User user = userService.getUserByUsername(username);  //这里在并发的过程中，如果有人同时申请两个同样的用户名字，就会出现数据库重复的情况，可以通过数据库约束完成
//        if (user == null) {
//            userService.save(username, password);
//            return new Result("ok", "success!", false);//查看用户是否在数据库
//        } else {
//            return new Result("fail", "user already exists", false);
//        }

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
            return Result.failure("用户不存在");
        }   //查看用户是否在数据库

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken
                (userDetails, password, userDetails.getAuthorities());  //鉴权，检验密码是否匹配该用户
        try {
            authenticationManager.authenticate(token); //完成鉴权的管理
            SecurityContextHolder.getContext().setAuthentication(token); //鉴别完成后，进行数据处理并保存
            //登录成功后
            return Result.success("登录成功", userService.getUserByUsername(username));
        } catch (BadCredentialsException e) {
            return Result.failure("密码不正确");
        }
    }


    @GetMapping(value = "/auth", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object auth(ModelMap map) {
        //String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        //User loggedInUser = userService.getUserByUsername(userName);
        // 在没有登录的时候，Authentication(）会出现控制正异常的行为，导致test无法进行
        // cookie

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userService.getUserByUsername(authentication == null ?
                null : authentication.getName());

        if (loggedInUser == null) {
            return new Result("ok", "用户没有登录", false);
        } else {
            return new Result("ok", true, loggedInUser);
        }
    }

    @GetMapping("/auth/logout")
    @ResponseBody
    public Result logout() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedInUser = userService.getUserByUsername(userName);

        if (loggedInUser == null) {
            return new Result("fail", "用户尚未登录");
        } else {
            SecurityContextHolder.clearContext();
            return new Result("ok", "注销成功");
        }
    }
}
