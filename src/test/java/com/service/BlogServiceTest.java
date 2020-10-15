package com.service;

import com.Dao.BlogDao;

import com.entity.Blog;
import com.entity.result.Result;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.List;


import static org.mockito.ArgumentMatchers.anyInt;


@ExtendWith(MockitoExtension.class)
public class BlogServiceTest {
    @Mock
    BlogDao blogDao;
    @InjectMocks
    BlogService blogService;

    @Test
    public void getBlogsByBlogUserId() {
        blogService.getBlogs(1, BigInteger.valueOf(10));
        Mockito.verify(blogDao).getBlogs(1, 10, BigInteger.valueOf(10), 1);
    }

    @Test
    public void returnFailureWhenExceptionThrow() {
        Mockito.when(blogDao.getBlogs(anyInt(), anyInt(), BigInteger.valueOf(anyInt()), anyInt()))
            .thenThrow(new RuntimeException());
        Result<List<Blog>> result = blogService.getBlogs(1, BigInteger.valueOf(10));
        Assertions.assertEquals("fail", result.getStatus());
        Assertions.assertEquals("系统异常", result.getMsg());
    }

}
