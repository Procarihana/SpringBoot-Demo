package com.service;

import com.entity.User;
import com.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    BCryptPasswordEncoder mockEncoder;
    @Mock
    UserMapper mockMapper;
    @InjectMocks  //所依赖的都是mock的
            UserService userService;

    @Test
    public void testSave() {
        //调用userService
        //验证userService将请求转发给userMapper


        //BCryptPasswordEncoder会因为输入的是Mock，所以加密后返回的回是null，需要额外给条件
        //given
        Mockito.when(mockEncoder.encode("MockPassword")).thenReturn("MockEncodedPassword");
        //when:
        userService.save("MockUser", "MockPassword");
        //then:
        Mockito.verify(mockMapper).save("MockUser", "MockEncodedPassword");
    }

    @Test
    public void testGetUserByUsername() {
        userService.getUserByUsername("MockUser");
        Mockito.verify(mockMapper).findUserByUsername("MockUser");
    }

    @Test
    public void throwExceptionWhenUserNoFount() {
        Mockito.when(mockMapper.findUserByUsername("MockUser")).thenReturn(null);
//        try {
//            userService.loadUserByUsername("MockUser");
//            Assertions.assertFalse(false);
//        } catch (UsernameNotFoundException e) {
//        }


        Assertions.assertThrows(UsernameNotFoundException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                userService.loadUserByUsername("MockUser");
            }
        });
        // 断言 userService.loadUserByUsername("MockUser") 调用 一定会丢出  UsernameNotFoundException.class异常，

    }

    @Test
    public void returnUserDetailWhenUserFound() {
        Mockito.when(mockMapper.findUserByUsername("MockUser")).thenReturn(new User(1212, "MockUser", "MockEncodedPassword"));

        UserDetails userDetails = userService.loadUserByUsername("MockUser");
        Assertions.assertEquals("MockUser", userDetails.getUsername());
        Assertions.assertEquals("MockEncodedPassword", userDetails.getPassword());
    }

}