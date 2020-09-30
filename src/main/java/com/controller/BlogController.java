package com.controller;

import com.entity.Service.Blog;
import com.entity.User;
import com.entity.result.BlogResult;
import com.entity.result.Result;
import com.fasterxml.jackson.databind.node.BigIntegerNode;
import com.service.BlogService;
import com.service.UserService;
import com.sun.org.apache.bcel.internal.generic.ARETURN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.websocket.server.PathParam;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
public class BlogController {
    private static final String ATINDEX = "true";
    @Autowired
    private BlogService blogService;
    @Autowired
    private UserService userService;

    @Inject
    public BlogController(BlogService blogService, UserService userService) {
        this.blogService = blogService;
        this.userService = userService;
    }

    @GetMapping(value = "/blog", produces = "application/json; charset=utf8")
    @ResponseBody
    public Result getBlogs(@RequestParam("page") Integer page,
                           @RequestParam(value = "userId", required = false) BigInteger userId,
                           @RequestParam(value = "atIndex", required = false) String atIndex) {
        if (atIndex == "true") {
            userId = BigInteger.valueOf(0);
        }
        return blogService.getBlogs(page, 10, userId);
    }

    @GetMapping(path = "/blog/{blogId}", produces = "application/x-www-form-urlencoded;charset=utf-8")
    @ResponseBody
    public Result getNewBlog(@PathVariable("blogId") Integer blogId) {
        BigInteger id = BigInteger.valueOf(blogId);
        List<Blog> result = new ArrayList<>();
        try {
            result = blogService.getBlogByBolgId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BlogResult.getBlogs(result, 1, 0, 1);
    }

    @PostMapping(value = "/blog", produces = "application/json; charset=utf-8", consumes = "application/json; charset=utf-8")
    @ResponseBody
    public Result createBlog(@RequestBody Map<String, Object> blog) {
        Boolean atIndex = (Boolean) blog.get("atIndex");
        String content = blog.get("content").toString();
        String description = blog.get("description").toString();
        String title = blog.get("title").toString();
        if (content.length() < 200 || content.length() > 1000) {
            return BlogResult.failure("博客内容不能为空，至少要200个字,不能超过1000个字");
        }
        if (title.length() < 5 || title.length() > 30) {
            return BlogResult.failure("博客标题不能为空，至少要5个字,不能超过30个字");
        }
        if (description.length() < 1 || description.length() > 30) {
            return BlogResult.failure("博客简介不能为空，限制在30个字以内");
        }
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        BigInteger userId = user.getId();
        blogService.save(atIndex, title, description, content, userId);
        Blog newBlog = blogService.getNewBlog(userId);
        newBlog.setUser(userService.getUserById(userId));
        List<Blog> result = new ArrayList<>();
        result.add(newBlog);
        return BlogResult.getBlogs(result, 1, 1, 1);
    }
}
