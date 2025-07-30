package com.beetles.filter;

import com.alibaba.fastjson2.JSON;
import com.beetles.DTO.LoginUser;
import com.beetles.utils.JwtUtil;
import com.beetles.utils.RedisCache;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private RedisCache redisCache;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取请求路径
        String requestURI = request.getRequestURI();

        // 如果是 /login 请求，直接放行
        if ("/login".equals(requestURI) ||
                requestURI.equals("/getDetailsInfoList") ||
                requestURI.equals("/getCommentsList") ||
                requestURI.equals("/getCommentsDanmakus") ||
                requestURI.equals("/register") ||
                requestURI.equals("/getDetailsContent") ||
                requestURI.equals("/webSocket")) {
            filterChain.doFilter(request, response);
            return;
        }

        //获取token
        String token = request.getHeader("Jwttoken");
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(request, response);
            System.out.println("token为空");
            return;
        }
        //解析token
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        //从redis中获取用户信息
        String redisKey = "login:" + userid;
//        LoginUser loginUser = redisCache.getCacheObject(redisKey);

        LoginUser loginUser = JSON.toJavaObject(redisCache.getCacheObject(redisKey), LoginUser.class);
        if(Objects.isNull(loginUser)){
            throw new RuntimeException("用户未登录或者登录超时，请重新登录");
        }
        //存入SecurityContextHolder
        //获取权限信息封装到Authentication中
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }
}