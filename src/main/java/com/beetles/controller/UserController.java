package com.beetles.controller;

import com.beetles.DTO.Result;
import com.beetles.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/getUserInfo")
    public Result<?> getUserId(@RequestParam("userName") String userName){
        return userService.getUserId(userName);
    }

    @RequestMapping("/getUserDetailsInfoList")
    public Result<?> getUserDetailsInfoList(@RequestBody Map<String, Object> requestBody){
        String authorId = (String) requestBody.get("authorId");
        String userId = (String) requestBody.get("userId");
        String type = (String) requestBody.get("type");
        return userService.getUserDetailsInfoList(authorId, userId);
    }

    @RequestMapping("/getUserShortInfo")
    public Result<?> getUserShortInfo(@RequestBody Map<String, Object> requestBody){
        String userId = (String) requestBody.get("userId");
        return userService.getUserShortInfo(userId);
    }

    /**
     * 检查用户是否关注
     */
    @RequestMapping("/checkIsFollowed")
    public Result<?> checkIsFollowed(@RequestBody Map<String, Object> requestBody){
        String userId = (String) requestBody.get("userId");
        String followerId = (String) requestBody.get("followerId");
        return userService.checkIsFollowed(userId, followerId);
    }

    /**
     * 添加用户关注
     */
    @RequestMapping("/addUserFollows")
    public Result<?> addUserFollows(@RequestBody Map<String, Object> requestBody){
        // userId:被关注的人id
        // followerId:关注的人id
        String userId = (String) requestBody.get("userId");
        String followerId = (String) requestBody.get("followerId");
        return userService.addUserFollows(userId, followerId);
    }

    /**
     * 移除用户关注
     */
    @RequestMapping("/removeUserFollows")
    public Result<?> removeUserFollows(@RequestBody Map<String, Object> requestBody){
        // userId:被关注者的id
        // followerId:关注者的id
        String userId = (String) requestBody.get("userId");
        String followerId = (String) requestBody.get("followerId");
        return userService.removeUserFollows(userId, followerId);
    }

    /**
     * 更改用户信息
     */
    @RequestMapping("/changeUserInfo")
    public Result<?> changeUserInfo(@RequestBody Map<String, Object> requestBody){
        // userNickname:用户昵称
        // sex:性别
        // bio:简介
        // email:邮箱
        // phonenumber:电话
        String type = (String) requestBody.get("type");
        String value = (String) requestBody.get("value");
        String userId = (String) requestBody.get("userId");
        return userService.changeUserInfo(type, value, userId);
    }

    /**
     * 根据id获取用户详细信息
     */
    @RequestMapping("/getUserInfoById")
    public Result<?> getUserInfoById(@RequestBody Map<String, Object> requestBody){
        String userId = (String) requestBody.get("userId");
        return userService.getUserInfoById(userId);
    }
}
