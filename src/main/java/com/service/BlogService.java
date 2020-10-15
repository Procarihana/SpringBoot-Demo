package com.service;

import com.Dao.BlogDao;

import com.converter.blogInfoPToS.BlogInfoPToSConverter;
import com.entity.Service.Blog;
import com.entity.result.BlogsResult;
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

    public Result getBlogs(Integer page, BigInteger userId) {
        Integer PAGE_SIZE = 10;
        page = (page - 1) * PAGE_SIZE;
        List<com.entity.Blog> blogs;
        try {
            if (userId.equals(BigInteger.valueOf(0))) {
                blogs = blogDao.getAtIndexBlogs(page, PAGE_SIZE);
            } else {
                blogs = blogDao.getBlogs(page, PAGE_SIZE, userId, 1);
            }
            List<Blog> result = new ArrayList<>();
            for (com.entity.Blog blog : blogs) {
                blog.setUser(userService.getUserById(BigInteger.valueOf(blog.getUserId().intValue())));
                result.add(converter.convert(blog));
            }
            //每一个blog 都根据userID 给一个user
            //但是每一个blog 都进行一次io数据库查询消耗非常大
            int count = blogDao.count(userId, 1);
            int totalPage = count % PAGE_SIZE == 0 ? count / PAGE_SIZE : count / PAGE_SIZE + 1;
            return BlogsResult.getBlogs(count, page, totalPage, result);
        } catch (Exception e) {
            e.printStackTrace();
            return BlogsResult.failure("系统异常");
        }
    }

    public Blog getBlogByBolgId(BigInteger blogId) {
        com.entity.Blog blog = blogDao.getBlogByBlogId(blogId);
        blog.setUser(userService.getUserById(blog.getUserId()));
        return converter.convert(blog);
    }

    public void save(Boolean atIndex, String title, String description, String content, BigInteger userId) {
        blogDao.save(atIndex, title, description, content, userId);
    }

    public Blog getNewBlog(BigInteger userId) {
        return converter.convert(blogDao.getNewBlog(userId));
    }

    public Blog updateBlog(Integer atIndex, String content, String description, String title,
                           BigInteger updatedBlogId) {
        return converter.convert(blogDao.updateBlog(atIndex, content, description, title, updatedBlogId));
    }

    public void deleteBlogByBlogId(BigInteger deleteBlogId) {
        blogDao.deleteBlogByBlogId(deleteBlogId);
    }
}
