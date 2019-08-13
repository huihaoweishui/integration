package com.example.integration;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

// required a single bean, but 2 were found @MapperScan 不要重复扫描 会生成多个bean -> https://www.jianshu.com/p/ce89aa265523
@SpringBootApplication
@MapperScan("com.example.integration.mapper")
@ServletComponentScan(basePackages = {"com.example.integration.config.servlet"})
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
