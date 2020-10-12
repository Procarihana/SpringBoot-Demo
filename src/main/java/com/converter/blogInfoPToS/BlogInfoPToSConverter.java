package com.converter.blogInfoPToS;


import com.entity.Service.Blog;
import com.google.common.base.Converter;
import org.springframework.stereotype.Component;

@Component
public class BlogInfoPToSConverter extends Converter<com.entity.Blog, Blog> {
    private static final Integer INDEX = 1;
    private static final Integer NOINDEX = 0;
    private static final Boolean atIndex = Boolean.TRUE;

    @Override
    protected Blog doForward(com.entity.Blog blog) {
        return Blog.builder().content(
            blog
                .getContent())
            .createdAt(blog.getCreatedAt())
            .description(blog.getDescription())
            .id(blog.getId())
            .updatedAt(blog.getUpdatedAt())
            .title(blog.getTitle())
            .user(blog.getUser())
            .userId(blog.getUserId())
            .build();
    }

    @Override
    protected com.entity.Blog doBackward(Blog blog) {
        return com.entity.Blog.builder()
            .content(blog.getContent())
            .createdAt(blog.getCreatedAt())
            .description(blog.getDescription())
            .id(blog.getId())
            .atIndex(blog.getAtIndex().equals(atIndex) ? INDEX : NOINDEX)
            .title(blog.getTitle())
            .updatedAt(blog.getUpdatedAt())
            .user(blog.getUser())
            .userId(blog.getId())
            .build();
    }
}
