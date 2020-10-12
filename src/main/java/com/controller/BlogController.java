package com.controller;

import com.entity.Service.Blog;
import com.entity.User;
import com.entity.result.BlogResult;
import com.entity.result.BlogsResult;
import com.entity.result.Result;
import com.service.BlogService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

import java.math.BigInteger;
import java.util.Map;


@Controller
public class BlogController {
    private static final String AT_INDEX = "true";
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
        if (atIndex == AT_INDEX) {
            userId = BigInteger.valueOf(0);
        }
        return blogService.getBlogs(page, userId);
    }

    @GetMapping(path = "/blog/{blogId}", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result getBlogByBlogId(@PathVariable("blogId") Integer blogId) {
        BigInteger id = BigInteger.valueOf(blogId);
        Blog blog = null;
        try {
            blog = blogService.getBlogByBolgId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BlogResult.getBlogByBlogId("获取成功", blog);
    }

    @PatchMapping(value = "/blog/{blogId}",
        consumes = "application/json; charset=utf-8",
        produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result updateBlog(@PathVariable("blogId") Integer blogId, @RequestBody Map<String, String> blog) {
        Integer isIndex = 0;
        Blog updatedBlog = blogService.getBlogByBolgId(BigInteger.valueOf(blogId));
        String atIndex = blog.get("atIndex");
        String content = blog.get("content");
        String description = blog.get("description");
        String title = blog.get("title");
        BigInteger updatedBlogUserId = updatedBlog.getUserId();
        BigInteger userId;
        BigInteger updatedBlogId;
        try {
            userId = userService.getUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()).getId();
            updatedBlogId = updatedBlog.getId();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return BlogResult.failure("登录后才能操作");
        }
        Result blogPresentAndBelongTheUser =
            checkBlogPresentAndBelongTheUser(blogId, updatedBlogUserId, userId, "修改");
        if (blogPresentAndBelongTheUser != null) {
            return blogPresentAndBelongTheUser;
        }
        if (atIndex.equals(AT_INDEX)) {
            isIndex = 1;
        }
        Blog newBlog = blogService.updateBlog(isIndex, content, description, title, updatedBlogId);
        return BlogResult.getBlogByBlogId("修改成功", newBlog);
    }

    @DeleteMapping(value = "/blog/{blogId}",
        consumes = "application/json; charset=utf-8",
        produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result deleteBlog(@PathVariable("blogId") Integer blogId) {
        Blog deleteBlog = blogService.getBlogByBolgId(BigInteger.valueOf(blogId));
        BigInteger userId;
        BigInteger deleteBlogId;
        BigInteger deleteBlogUserId = deleteBlog.getUserId();
        try {
            userId = userService.getUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()).getId();
            deleteBlogId = deleteBlog.getId();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return BlogResult.failure("登录后才能操作");
        }
        Result checkBlogPresentAndBelongTheUser =
            checkBlogPresentAndBelongTheUser(blogId, deleteBlogUserId, userId, "删除");
        if (checkBlogPresentAndBelongTheUser != null) {
            return checkBlogPresentAndBelongTheUser;
        }
        blogService.deleteBlogByBlogId(deleteBlogId);
        try {
            blogService.getBlogByBolgId(deleteBlogId);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return BlogResult.deleteSuccess();
        }
        return BlogResult.failure("系统错误");
    }

    @PostMapping(value = "/blog", produces = "application/json; charset=utf-8", consumes = "application/json; charset=utf-8")
    @ResponseBody
    public Result createBlog(@RequestBody Map<String, Object> blog) {
        Boolean atIndex = (Boolean) blog.get("atIndex");
        String content = blog.get("content").toString();
        String description = blog.get("description").toString();
        String title = blog.get("title").toString();
        Result failure = getResult(content, description, title);
        if (failure != null) {
            return failure;
        }
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        BigInteger userId = user.getId();
        blogService.save(atIndex, title, description, content, userId);
        Blog newBlog = blogService.getNewBlog(userId);
        newBlog.setUser(userService.getUserById(userId));
        return BlogResult.getBlogByBlogId("创建成功", newBlog);
    }

    private Result getResult(String content, String description, String title) {
        if (content.length() < 200 || content.length() > 1000) {
            return BlogsResult.failure("博客内容不能为空，至少要200个字,不能超过1000个字");
        }
        if (title.length() < 5 || title.length() > 30) {
            return BlogsResult.failure("博客标题不能为空，至少要5个字,不能超过30个字");
        }
        if (description.length() < 1 || description.length() > 30) {
            return BlogsResult.failure("博客简介不能为空，限制在30个字以内");
        }
        return null;
    }

    private Result checkBlogPresentAndBelongTheUser(Integer blogId, BigInteger updatedBlogUserId,
                                                    BigInteger userId, String change) {
        String testBlogIsNull = "";
        if (testBlogIsNull.equals(blogService.getBlogByBolgId(BigInteger.valueOf(blogId)).getTitle())) {
            return BlogResult.failure("博客不存在");
        }
        if (!updatedBlogUserId.equals(userId)) {
            return BlogResult.failure("无法" + change + "别人的博客");
        }
        return null;
    }


}
