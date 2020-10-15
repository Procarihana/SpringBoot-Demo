package com.Dao;

import java.math.BigInteger;

import com.entity.User;
import com.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDao {
    @Autowired
    private UserMapper userMapper;

    public UserDao(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public void save(String username, String bCryptPasswordEncoder, String avatar) {
        userMapper.save(username, bCryptPasswordEncoder, avatar);
    }

    public User findUserByUsername(String username) {
        return userMapper.findUserByUsername(username);
    }

    public User getUserById(BigInteger userId) {
        return userMapper.getUserById((userId));
    }
}
