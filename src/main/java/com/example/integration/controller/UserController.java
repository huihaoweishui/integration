package com.example.integration.controller;

import com.example.integration.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    // 测试nginx负载均衡
    @GetMapping("test")
    public String test(HttpServletRequest request) {
        return userMapper.selectAll().toString() + request.getLocalPort();
    }

    @GetMapping("add")
    public String add(String name) {
        userMapper.addUser(name);
        return "success";
    }
}
