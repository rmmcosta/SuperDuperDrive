package com.rmmcosta.superduperdrive.mapper;

import com.rmmcosta.superduperdrive.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE username = #{username}")
    User getUser(String username);

    @Insert("INSERT INTO users (username, salt, password, f_name, l_name) values(#{username}, #{salt}, #{password}, #{fName}, #{lName})")
    @Options(useGeneratedKeys = true, keyProperty = "user_id")
    int insertUser(User user);
}
