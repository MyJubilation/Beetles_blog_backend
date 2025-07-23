package com.beetles.controller;

import com.beetles.DTO.Result;
import com.beetles.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/getUserId")
    public Result<?> getUserId(@RequestParam("userName") String userName){
        return userService.getUserId(userName);
    }
}
