package com.mapper;


import java.math.BigInteger;

import com.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM user WHERE id = #{id}")
    User findUserById(@Param("id") Integer id);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User getUserById(@Param("id") BigInteger id);

    @Select("SELECT * FROM user WHERE username = #{username}")
    User findUserByUsername(@Param("username") String username);

    @Insert("Insert into user(username,encrypted_password,avatar,created_at,updated_at)" +
        "values(#{username}, #{encrypted_password}, #{avatar},now(),now())")
    void save(@Param("username") String username, @Param("encrypted_password") String encryptedPassword,
              @Param("avatar") String avatar);

}
