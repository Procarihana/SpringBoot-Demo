package com.service;

import com.Dao.UserDao;
import com.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import java.math.BigInteger;
import java.util.Collections;


@Service
public class UserService implements UserDetailsService {
    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private final UserDao userDao;

    @Inject
    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserDao userDao) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userDao = userDao;
    }

    @Transactional
    public String save(String username, String password) {
        String avatar = "https://avatars.dicebear.com/api/male/" + username + ".svg";
        userDao.save(username, bCryptPasswordEncoder.encode(password), avatar);
        return "success!";
    }

    public User getUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }

    public User getUserById(BigInteger userId) {
        return userDao.getUserById((userId));
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
