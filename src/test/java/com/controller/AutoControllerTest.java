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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class AutoControllerTest {
    private MockMvc mockMvc;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;

    private final String MOCK_USERNAME = "mockUserName";
    private final String MOCK_PASSWORD = "mockPassword";
    private final String WRONG_USERNAME = "wrongUsername";
    private final String WRONG_PASSWORD = "wrongPassword";

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
        MockHttpSession session = (MockHttpSession) loginSuccessAndGetSession(MOCK_USERNAME, MOCK_PASSWORD);
        //登录后再一次进行`/auth`访问
        getMethodAndResult("/auth", session, MOCK_USERNAME);
    }

    @Test
    void testLonginUserNotExist() throws Exception {
        Map<String, String> insertMessage = UserInMap(WRONG_USERNAME, WRONG_PASSWORD);
        Mockito.when(userService.loadUserByUsername(insertMessage.get("username")))
            .thenThrow(new UsernameNotFoundException("用户不存在"));
        postMethodAndGetResponse("/auth/login", insertMessage, "用户不存在");
    }


    @Test
    void testLoginWithWrongPassword() throws Exception {
        loadUserByUsername(MOCK_USERNAME, MOCK_PASSWORD);
        failToLogin(MOCK_USERNAME, MOCK_PASSWORD, "密码不正确");
        postMethodAndGetResponse("/auth/login", UserInMap(MOCK_USERNAME, WRONG_PASSWORD), "密码不正确");
    }


    @Test
    void testLogoutSuccess() throws Exception {
        MockHttpSession session = (MockHttpSession) loginSuccessAndGetSession(MOCK_USERNAME, MOCK_PASSWORD);
        getMethodAndResult("/auth/logout", session, "注销成功");
    }

    @Test
    void testRegisterSuccess() throws Exception {
        Mockito.when(userService.save(MOCK_USERNAME, MOCK_PASSWORD)).thenReturn("success!");
        loadUserByUsername(MOCK_USERNAME, MOCK_PASSWORD);
        postMethodAndGetResponse("/auth/register", UserInMap(MOCK_USERNAME, MOCK_PASSWORD), "注册成功");
    }


    private HttpSession loginSuccessAndGetSession(String username, String password) throws Exception {
        loadUserByUsername(username, password);
        getEntityUserByUsername(username, password);
        MvcResult response = postMethodAndGetResponse("/auth/login", UserInMap(username, password), "登录成功");
        // System.out.println(response.getResponse().getCookies());  //仅仅是进行单元测试，测试返回的内容，而鉴权和cookie 是没有实现的，所以返回的cookie是拿不到的
        HttpSession session = response.getRequest().getSession();  //session就是一组会话，HTTP响应和状态的集合
        return session;
    }

    private void loadUserByUsername(String username, String password) {
        Mockito.when(userService.loadUserByUsername(username))
            .thenReturn(new User(username, bCryptPasswordEncoder
                .encode(password), Collections.emptyList()));
    }

    private void getEntityUserByUsername(String username, String password) {
        Mockito.when(userService.getUserByUsername(username)).thenReturn(new com.entity
            .User(BigInteger.valueOf(123), username, bCryptPasswordEncoder
            .encode(password)));
    }

    private MvcResult postMethodAndGetResponse(String path, Map<String, String> usernamePassword, String message)
        throws Exception {
        MvcResult response = mockMvc.perform(post(path)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper()
                .writeValueAsString(usernamePassword)))
            .andExpect(status().isOk())
            .andExpect(new ResultMatcher() {
                @Override
                public void match(MvcResult mvcResult) throws Exception {
                    String result = mvcResult.getResponse().getContentAsString();
                    System.out.println(result);
                    Assertions.assertTrue(result.contains(message));
                }
            })
            .andReturn();
        return response;
    }

    private void getMethodAndResult(String path, MockHttpSession session, String message) throws Exception {
        mockMvc.perform(get(path).session(session))//在登录后再一次访问`/auth`
            .andExpect(status().isOk())
            .andExpect(
                mvcResult -> {
                    String result = mvcResult.getResponse().getContentAsString();
                    System.out.println(result);
                    Assertions.assertTrue(result.contains(message));
                });
    }

    private void unLoginStatusTest() throws Exception {
        mockMvc.perform(get("/auth"))
            .andExpect(status().isOk())
            .andExpect(
                mvcResult -> Assertions.assertTrue(mvcResult.getResponse().getContentAsString().contains("用户没有登录")));
    }

    private Map<String, String> UserInMap(String username, String password) {
        Map<String, String> usernamePassword = new HashMap<>(); //Jason序列化可以通过Map实现
        usernamePassword.put("username", username);
        usernamePassword.put("password", password);
        return usernamePassword;
    }

    private void failToLogin(String username, String password, String message) {
        UserDetails userDetails = new User(username, password, Collections.emptyList());
        UsernamePasswordAuthenticationToken token =
            new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        Mockito.when(authenticationManager.authenticate(token)).thenThrow(new BadCredentialsException(message));
    }

}