package com.Dao;

import com.converter.blogInfoPToS.BlogInfoPToSConverter;
import com.entity.Blog;
import com.mapper.BlogMapper;
import org.apache.ibatis.session.SqlSession;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogDao {
    private final SqlSession sqlSession;
    private final BlogMapper blogMapper;
    private final Integer ATINDEX = 1;
    private final Integer NOTINDEX = 0;
    private final BlogInfoPToSConverter converter;

    @Inject
    public BlogDao(SqlSession sqlSession, BlogMapper blogMapper, BlogInfoPToSConverter converter) {
        this.sqlSession = sqlSession;
        this.blogMapper = blogMapper;
        this.converter = converter;
    }


    public List<Blog> getBlogs(Integer page, Integer pageSize, BigInteger userId, Integer atIndex) {
        Map<String, Object> selectBlog = new HashMap<>();
        selectBlog.put("user_id", userId);
        selectBlog.put("offset", (page - 1) * pageSize);
        selectBlog.put("limit", pageSize);
        selectBlog.put("at_index", atIndex);
        List<Blog> blogs = sqlSession.selectList("db.mybatis.mapper.BlogMapper.selectBlog", selectBlog);
        return blogs;
    }

    public int count(BigInteger userId, Integer atIndex) {
        if (userId == null) {
            return sqlSession.selectOne("count", atIndex);
        }
        return sqlSession.selectOne("count", userId);
    }

    public Blog getBlogByBlogId(BigInteger blogId) {
        Blog blog = blogMapper.getBlogByBlogId(blogId);
        return blog;

    }

    public void save(Boolean atIndex, String title, String description, String content, BigInteger userId) {
        Integer isIndex = null;
        if (Boolean.TRUE.equals(atIndex)) {
            isIndex = ATINDEX;
        } else {
            isIndex = NOTINDEX;
        }
        blogMapper.save(isIndex, title, description, content, userId);
    }

    public com.entity.Service.Blog getBlogByBlogTitle(String title) {
        Blog blog = blogMapper.getBlogByBlogTitle(title);
        return converter.convert(blog);
    }

    public List<Blog> getAtIndexBlogs(Integer atIndex, Integer page, Integer pageSize) {
        List<Blog> blogs = new ArrayList<>();
        blogs.addAll(blogMapper.getAtIndexBlogs(atIndex, page, pageSize));
        return blogs;
    }

    public Blog getNewBlog(BigInteger userId) {
        return blogMapper.getNewBlog(userId);
    }
}
