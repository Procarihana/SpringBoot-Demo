package com.entity;

import java.util.List;

public class BlogResult extends Result<List<Blog>> {
    private int total;
    private int page;
    private int totalPage;

    public static BlogResult newBlogs(List<Blog> data, int total, int page, int totalPage) {
        return new BlogResult("ok","获得成功",data, total, page, totalPage);  //能够return null
    }
    public static BlogResult failure (String message){
        return new BlogResult("fail",message,null,0,0,0);
    }

    private BlogResult(String status,String message,List<Blog> data, int total, int page, int totalPage) { //通过工厂方法，名字能够清楚告诉方法的业务，且能够进行复杂的逻辑，还能够返回null
        super(status, message, data);
        this.total = total;
        this.page = page;
        this.totalPage = totalPage;
    }

    public int getTotal() {
        return total;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPage() {
        return totalPage;
    }

}
