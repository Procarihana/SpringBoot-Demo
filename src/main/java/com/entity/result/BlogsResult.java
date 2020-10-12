package com.entity.result;

import java.util.List;

import com.entity.Service.Blog;

public class BlogsResult extends Result<List<Blog>> {
    private Integer total;
    private Integer page;
    private Integer totalPage;

    public static Result getBlogs(Integer total, Integer page, Integer totalPage, List<Blog> data) {
        return new BlogsResult("ok", "获取成功", total, page, totalPage, data);
    }

    public static Result create(List<Blog> data) {
        return new BlogsResult("ok", "创建成功", data);  //能够return null
    }

    public static Result failure(String message) {
        return new BlogsResult("fail", message);
    }

    public BlogsResult(String status, String msg, Integer total, Integer page, Integer totalPage, List<Blog> data) {
        super(status, msg, data);
        this.total = total;
        this.page = page;
        this.totalPage = totalPage;
    }

    public BlogsResult(String status, String msg) {
        super(status, msg);
    }

    private BlogsResult(String status, String message, List<Blog> data) {
        super(status, message, data);
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getTotalPage() {
        return totalPage;
    }
}
