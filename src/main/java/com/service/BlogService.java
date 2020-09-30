package com.service;

import com.Dao.BlogDao;


import com.converter.blogInfoPToS.BlogInfoPToSConverter;
import com.entity.Service.Blog;
import com.entity.result.BlogResult;
import com.entity.result.Result;
import org.springframework.stereotype.Service;


import javax.inject.Inject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class BlogService {
    private BlogDao blogDao;
    private UserService userService;
    private BlogInfoPToSConverter converter;

    @Inject
    public BlogService(BlogDao blogDao, UserService userService, BlogInfoPToSConverter converter) {
        this.blogDao = blogDao;
        this.userService = userService;
        this.converter = converter;
    }

    public Result getBlogs(Integer page, Integer pageSize, BigInteger userId) {
        List<com.entity.Blog> blogs;
        try {
            if (userId == null) {
                blogs = blogDao.getAtIndexBlogs(1, 1, 10);
            } else {
                blogs = blogDao.getBlogs(page, pageSize, userId, 1);
            }
            List<Blog> result = new ArrayList<>();
            for (com.entity.Blog blog : blogs) {
                blog.setUser(userService.getUserById(BigInteger.valueOf(blog.getUserId().intValue())));
                result.add(converter.convert(blog));
            }

            //每一个blog 都根据userID 给一个user
            //但是每一个blog 都进行一次io数据库查询消耗非常大
            int count = blogDao.count(userId, 1);
            int pageCount = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
            return BlogResult.getBlogs(result, count, page, pageCount);
        } catch (Exception e) {
            e.printStackTrace();
            return BlogResult.failure("系统异常");
        }
    }

    public List<Blog> getBlogByBolgId(BigInteger blogId) {
        List<Blog> result = new ArrayList<>();
        com.entity.Blog blog = blogDao.getBlogByBlogId(blogId);
        blog.setUser(userService.getUserById(blog.getUserId()));
        result.add(converter.convert(blog));
        return result;
    }

    public void save(Boolean atIndex, String title, String description, String content, BigInteger userId) {
        blogDao.save(atIndex, title, description, content, userId);
    }

    public Blog getNewBlog(BigInteger userId) {
        return converter.convert(blogDao.getNewBlog(userId));
    }
}
