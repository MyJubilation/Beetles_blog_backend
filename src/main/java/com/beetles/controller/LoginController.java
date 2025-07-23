package com.beetles.controller;


import com.beetles.DTO.Result;
import com.beetles.DTO.User;
import com.beetles.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginServcie;


    @RequestMapping("/login")
    public Result<?> login(@RequestBody User user){
        return loginServcie.login(user);
    }

    @GetMapping("/logout")
    public Result<?> logout(){
        return loginServcie.logout();
    }

    @RequestMapping("/register")
    public Result<?> register(@RequestBody User user){
        return loginServcie.register(user);
    }

    @RequestMapping("/checkIsLogin")
    public Result<?> checkIsLogin(){
        return new Result<>().success(200, "已登录");
    }

}
