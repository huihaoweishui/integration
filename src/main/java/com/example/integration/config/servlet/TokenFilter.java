package com.example.integration.config.servlet;

import com.example.integration.service.IRedisService;
import com.mysql.jdbc.StringUtils;
import jwt.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 　springboot中 过滤器配置方式有两种：
 * 　1、通过@WebFilter注解来配置
 * 　2、通过@Bean注解来配置
 * 这里使用第二种 因为要注入其他Bean
 */
@Slf4j
public class TokenFilter implements Filter {

    @Autowired
    private IRedisService redisService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestURI = httpServletRequest.getRequestURI();
        log.info(requestURI);
        // 1、 免登陆接口
        if (!StringUtils.isEmptyOrWhitespaceOnly(requestURI) && (requestURI.contains("withOutLogin"))) {
            chain.doFilter(request, response);
            return;
        }
        //2、 验证登陆token
        String header = httpServletRequest.getHeader("Authorization");
        //没有token
        if (StringUtils.isEmptyOrWhitespaceOnly(header)) {
            response.getWriter().print("not allowed");
            return;
        } else {
            Boolean pass = JWTUtil.verifyToken(header, "123456", "xc", "xc");
            if (!pass) {
                response.getWriter().print("token wrong");
                return;
            } else {
                // 验证是否超时
                String token = redisService.get("token");
                if (StringUtils.isEmptyOrWhitespaceOnly(token)) {
                    response.getWriter().print("token expired");
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
