package com.Dao;

import com.entity.Blog;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogDao {
    private final SqlSession sqlSession;

    @Inject
    public BlogDao(SqlSession sqlsession) {
        this.sqlSession = sqlsession;
    }

    public List<Blog> getBlogs(Integer page, Integer pageSize, BigInteger userId) {
        Map<String, Object> selectBlog = new HashMap<>();
        selectBlog.put("user_id", userId);
        selectBlog.put("offset",  (page - 1) * pageSize);
        selectBlog.put("limit", pageSize);
        return sqlSession.selectList("db.mybatis.mapper.BlogMapper.selectBlog", selectBlog);
    }

    public int count(BigInteger userId)  {
        return sqlSession.selectOne("countBlog", userId);
    }

}
