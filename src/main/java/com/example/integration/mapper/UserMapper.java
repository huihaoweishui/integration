package com.example.integration.mapper;

import com.example.integration.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {

    @Select("select id,name from user")
    List<User> selectAll();

    @Insert("insert into user(name) values(#{name})")
    int addUser(String name);
}
