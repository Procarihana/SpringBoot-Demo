package com.mapper;

import java.math.BigInteger;
import java.util.List;

import com.entity.Blog;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface BlogMapper {
    @Select("SELECT * FROM `blog` WHERE id = #{id}")
    @Results({
        @Result(column = "id", property = "id"),
        @Result(column = "user_id", property = "userId"),
        @Result(column = "title", property = "title"),
        @Result(column = "description", property = "description"),
        @Result(column = "context", property = "context"),
        @Result(column = "at_index", property = "atIndex"),
        @Result(column = "created_at", property = "createdAt"),
        @Result(column = "updated_at", property = "updatedAt")
    })
    Blog getBlogByBlogId(@Param("id") BigInteger id);

    @Insert("insert into `blog` " +
        "(user_id,title,description,content,at_index,created_at) " +
        "value(#{userId},#{title},#{description},#{content},#{at_index},NOW())")
    @Results({
        @Result(column = "id", property = "id"),
        @Result(column = "user_id", property = "userId"),
        @Result(column = "title", property = "title"),
        @Result(column = "description", property = "description"),
        @Result(column = "context", property = "context"),
        @Result(column = "at_index", property = "atIndex"),
        @Result(column = "created_at", property = "createdAt"),
        @Result(column = "updated_at", property = "updatedAt")
    })
    void save(@Param("at_index") Integer atIndex,
              @Param("title") String title,
              @Param("description") String description,
              @Param("content") String content,
              @Param("userId") BigInteger userId);


    @Select("SELECT * FROM `blog` WHERE title = #{title}")
    @Results({
        @Result(column = "id", property = "id"),
        @Result(column = "user_id", property = "userId"),
        @Result(column = "title", property = "title"),
        @Result(column = "description", property = "description"),
        @Result(column = "context", property = "context"),
        @Result(column = "at_index", property = "atIndex"),
        @Result(column = "created_at", property = "createdAt"),
        @Result(column = "updated_at", property = "updatedAt")
    })
    Blog getBlogByBlogTitle(@Param("title") String title);

    @Select("SELECT * FROM `blog` WHERE at_index = 1 LIMIT #{offset} , #{limit}")
    @Results({
        @Result(column = "id", property = "id"),
        @Result(column = "user_id", property = "userId"),
        @Result(column = "title", property = "title"),
        @Result(column = "description", property = "description"),
        @Result(column = "context", property = "context"),
        @Result(column = "at_index", property = "atIndex"),
        @Result(column = "created_at", property = "createdAt"),
        @Result(column = "updated_at", property = "updatedAt")
    })
    List<Blog> getAtIndexBlogs(@Param("offset") Integer page,
                               @Param("limit") Integer pageSize);

    @Select("SELECT  * FROM `blog` where user_id = #{user_id} ORDER BY id desc limit 1")
    @Results({
        @Result(column = "id", property = "id"),
        @Result(column = "user_id", property = "userId"),
        @Result(column = "title", property = "title"),
        @Result(column = "description", property = "description"),
        @Result(column = "context", property = "context"),
        @Result(column = "at_index", property = "atIndex"),
        @Result(column = "created_at", property = "createdAt"),
        @Result(column = "updated_at", property = "updatedAt")
    })
    Blog getNewBlog(@Param("user_id") BigInteger userId);


    @Update("UPDATE `blog` SET title=#{title}, description=#{description}, " +
        "content=#{content}, at_index=#{atIndex}, updated_at=NOW() where id=#{updatedBlogId}")
    @Results({
        @Result(column = "id", property = "id"),
        @Result(column = "user_id", property = "userId"),
        @Result(column = "title", property = "title"),
        @Result(column = "description", property = "description"),
        @Result(column = "context", property = "context"),
        @Result(column = "at_index", property = "atIndex"),
        @Result(column = "created_at", property = "createdAt"),
        @Result(column = "updated_at", property = "updatedAt")
    })
    Integer updateBlog(@Param("atIndex") Integer atIndex,
                       @Param("content") String content,
                       @Param("description") String description,
                       @Param("title") String title,
                       @Param("updatedBlogId") BigInteger updatedBlogId);


    @Delete("DELETE from `blog` where id = #{blogId}")
    @Results({
        @Result(column = "id", property = "id"),
        @Result(column = "user_id", property = "userId"),
        @Result(column = "title", property = "title"),
        @Result(column = "description", property = "description"),
        @Result(column = "context", property = "context"),
        @Result(column = "at_index", property = "atIndex"),
        @Result(column = "created_at", property = "createdAt"),
        @Result(column = "updated_at", property = "updatedAt")
    })
    void deleteBlogByBlogId(@Param("blogId") BigInteger deleteBlogId);
}
