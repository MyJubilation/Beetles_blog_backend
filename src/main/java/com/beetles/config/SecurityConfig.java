package com.beetles.config;

import com.beetles.DTO.Result;
import com.beetles.filter.JwtAuthenticationTokenFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 关闭CSRF保护（适用于无状态的JWT认证）
            .csrf(csrf -> csrf.disable())
            // 配置会话管理为无状态
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            // 配置请求授权规则
            .authorizeHttpRequests(authorize -> authorize
                    // 允许匿名访问登录接口
                    .requestMatchers("/login").anonymous()
                    // 允许匿名访问接口
                    .requestMatchers("/getDetailsInfoList").permitAll()
                    .requestMatchers("/register",  "/getDetailsContent", "/webSocket").permitAll()


//                    // 允许WebSocket连接（例如 /webSocket）
//                    .requestMatchers().permitAll()
                    // 其他所有请求需要认证
                    .anyRequest().authenticated()
            )
            // 添加JWT过滤器到过滤器链中
            .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                // 自定义登出处理
            .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessHandler(new LogoutSuccessHandler() {
                        @Override
                        public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                            // 创建Result对象
                            Result<String> result = new Result<>();
                            result.success("Logout successful");

                            // 设置响应内容类型为JSON
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpServletResponse.SC_OK);

                            // 将Result对象转换为JSON并写入响应
                            ObjectMapper objectMapper = new ObjectMapper();
                            response.getWriter().write(objectMapper.writeValueAsString(result));
                        }
                    })
            );

        return http.build();
    }
}