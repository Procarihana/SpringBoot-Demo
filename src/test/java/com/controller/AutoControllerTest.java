package com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class AutoControllerTest {
    private MockMvc mockMvc;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(new AutoController(authenticationManager, userService))
            .build();
    }

    @Test
    void returnNotLoginByDefault() throws Exception {
        unLoginStatusTest();
    }


    @Test
    void testLogin() throws Exception {
        //未登录时，/auth接口返回为登录状态
        unLoginStatusTest();
        //使用/auth/login登录
        MvcResult response = Login();
        // System.out.println(response.getResponse().getCookies());  //仅仅是进行单元测试，测试返回的内容，而鉴权和cookie 是没有实现的，所以返回的cookie是拿不到的
        HttpSession session = response.getRequest().getSession();//session就是一组会话，HTTP响应和状态的集合
        //登录后再一次进行`/auth`访问
        checkLoginStatus((MockHttpSession) session);

    }

    private void checkLoginStatus(MockHttpSession session) throws Exception {
        mockMvc.perform(get("/auth").session(session))//在登录后再一次访问`/auth`
            .andExpect(status().isOk())
            .andExpect(
                mvcResult -> Assertions.assertTrue(mvcResult.getResponse().getContentAsString().contains("MockUser")));
    }

    private MvcResult Login() throws Exception {
        Map<String, String> usernamePassword = new HashMap<>(); //Jason序列化可以通过Map实现

        usernamePassword.put("username", "MockUser");
        usernamePassword.put("passwordgit stat", "MockPassword");
        Mockito.when(userService.loadUserByUsername("MockUser"))
            .thenReturn(new User("MockUser", bCryptPasswordEncoder
                .encode("MockPassword"), Collections.emptyList()));
        Mockito.when(userService.getUserByUsername("MockUser")).thenReturn(new com.entity
            .User(BigInteger.valueOf(123), "MockUser", bCryptPasswordEncoder
            .encode("MockPassword")));
        MvcResult response = mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper()
                .writeValueAsString(usernamePassword)))
            .andExpect(status().isOk())
            .andExpect(new ResultMatcher() {
                @Override
                public void match(MvcResult mvcResult) throws Exception {
                    Assertions.assertTrue(mvcResult.getResponse().getContentAsString().contains("登录成功"));
                }
            })
            .andReturn();
        return response;
    }

    private void unLoginStatusTest() throws Exception {
        mockMvc.perform(get("/auth"))
            .andExpect(status().isOk())
            .andExpect(
                mvcResult -> Assertions.assertTrue(mvcResult.getResponse().getContentAsString().contains("用户没有登录")));
    }
}