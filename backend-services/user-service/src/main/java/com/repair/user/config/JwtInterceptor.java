package com.repair.user.config;

import com.repair.common.exception.BusinessException;
import com.repair.user.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new BusinessException(401, "未登录或Token无效");
        }
        token = token.substring(7);
        if (jwtUtil.isTokenExpired(token)) {
            throw new BusinessException(401, "Token已过期，请重新登录");
        }
        request.setAttribute("userId", jwtUtil.parseToken(token).get("userId", Long.class));
        request.setAttribute("username", jwtUtil.parseToken(token).get("username", String.class));
        request.setAttribute("role", jwtUtil.parseToken(token).get("role", String.class));
        return true;
    }
}
