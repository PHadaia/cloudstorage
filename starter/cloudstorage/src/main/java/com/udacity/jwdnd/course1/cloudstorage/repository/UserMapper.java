package com.udacity.jwdnd.course1.cloudstorage.repository;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM USERS WHERE userId = #{userId}")
    User findUser(Integer userId);

    @Select("SELECT * FROM USERS WHERE username = #{username}")
    User findUserByUsername(String username);

    @Insert("INSERT INTO USERS (username, salt, password, firstname, lastname) " +
            "VALUES(#{username}, #{salt}, #{password}, #{firstname}, #{lastname})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    Integer insertUser(User user);

    @Delete("DELETE FROM USERS WHERE userId = #{userId}")
    void deleteUser(Integer userId);
}
