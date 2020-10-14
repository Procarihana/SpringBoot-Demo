package com.service;

import com.entity.User;
import com.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.math.BigInteger;
import java.util.Collections;


@Service
public class UserService implements UserDetailsService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserMapper userMapper;

    @Inject
    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserMapper userMapper) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userMapper = userMapper;
    }

    public String save(String username, String password) {
        String avatar = "https://avatars.dicebear.com/api/male/" + username + ".svg";
        userMapper.save(username, bCryptPasswordEncoder.encode(password),avatar);
        return "success!";
    }

    public User getUserByUsername(String username) {
        return userMapper.findUserByUsername(username);
    }

    public User getUserById(BigInteger userId) {
        return userMapper.getUserById((userId));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username + "不存在");

        }
        return new org.springframework.security.core.userdetails.User(username, user.getEncryptedPassword(),
            Collections.emptyList());
    }
}
