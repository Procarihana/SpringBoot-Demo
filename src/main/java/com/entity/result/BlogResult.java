package com.entity.result;

import java.util.List;

import com.entity.Service.Blog;

public class BlogResult extends Result<Blog> {

    public static Result getBlogByBlogId(String message, Blog data) {
        return new BlogResult("ok", message, data);  //能够return null
    }

    protected BlogResult(String status, String msg) {
        super(status, msg);
    }

    protected BlogResult(String status, String msg, Blog data) {
        super(status, msg, data);
    }

    public static Result failure(String message) {
        return new BlogResult("fail", message);
    }

    public static Result deleteSuccess() {
        return new BlogResult("ok", "删除成功");
    }
}
