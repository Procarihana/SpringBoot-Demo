package com.service;

import com.Dao.BlogDao;
import com.entity.Blog;
import com.entity.BlogResult;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigInteger;
import java.util.List;

@Service
public class BlogService {
    private BlogDao blogDao;
    private UserService userService;

    @Inject
    public BlogService(BlogDao blogDao, UserService userService) {
        this.blogDao = blogDao;
        this.userService = userService;
    }

    public BlogResult getBlogs(Integer page, Integer pageSize, BigInteger userId) {
        try {
            List<Blog> blogs = blogDao.getBlogs(page, pageSize, userId);
            blogs.forEach(blog ->
                    blog.setUser(userService.getUserById(blog.getUserId().intValue())));
            //每一个blog 都根据userID 给一个user
            //但是每一个blog 都进行一次io数据库查询消耗非常大
            int count = blogDao.count(userId);
            int pageCount = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
            return BlogResult.newBlogs(blogs, count, page, pageCount);
        } catch (Exception e) {
            e.printStackTrace();
            return BlogResult.failure("系统异常");


        }
    }
}
