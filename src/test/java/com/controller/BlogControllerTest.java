package com.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.math.BigInteger;

import com.entity.result.BlogResult;
import com.entity.result.BlogsResult;
import com.entity.result.Result;
import com.service.BlogService;
import com.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class BlogControllerTest {
    private MockMvc mockMvc;
    @Mock
    private BlogService blogService;
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;

    private final int MOCK_PAGE = 1;
    private final BigInteger MOCK_USER_ID = BigInteger.valueOf(1);
    private final String MOCK_STRING_USER_ID = "1";
    private final String MOCK_TITLE = "TestBlogController";
    private final String MOCK_BLOG_DETAIL = "MOCK_BLOG_DETAIL";
    private final String MOCK_COUNT = "1";
    private final String MOCK_TOTAL_PAGE = "1";


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(new BlogController(blogService, userService))
            .build();

    }

    @Test
    void testGetBlogByUserId() throws Exception {
        Mockito.when(blogService.getBlogs(MOCK_PAGE, MOCK_USER_ID))
            .thenReturn(new BlogResult("ok", "获得成功"));
        mockMvc.perform(
            MockMvcRequestBuilders.get("/blog")
                .param("page", String.valueOf(MOCK_PAGE))
                .param("userId", String.valueOf(MOCK_USER_ID))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().isOk())
            .andExpect(new ResultMatcher() {
                @Override
                public void match(MvcResult result) throws Exception {
                    Assertions.assertTrue(result.getResponse().getContentAsString().contains("获得成功"));
                }
            });
    }
}
