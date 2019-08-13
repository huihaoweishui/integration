package com.example.integration.config.servlet;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

//https://my.oschina.net/goulin/blog/1820070 就是回到原来的手动方式声明filter
@Configuration // 如不想使用TokenFilter 注释该注解即可
public class FilterConfig {

    @Bean(name = "tokenFilter")
    public TokenFilter create() {
        return new TokenFilter();
    }

    @Bean
    public FilterRegistrationBean registrationTokenFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new DelegatingFilterProxy("tokenFilter"));
        registration.addUrlPatterns("/*");
        registration.setName("tokenFilter");
        registration.setOrder(1);
        return registration;
    }
}
