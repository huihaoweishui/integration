package com.example.integration.controller;

import com.example.integration.service.IRedisService;
import com.xc.utils.jwt.MyJWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/withOutLogin")
public class WithOutLoginController {

    @Autowired
    private IRedisService redisService;

    @GetMapping("getToken")
    public String getToken() {
        String token = MyJWTUtil.createToken("123456", "xc", "xc", new HashMap<>());
        redisService.set("token", token);
        redisService.expire("token", 300);
        return token;
    }
}
