package com.beetles.service.impl;

import com.beetles.DTO.Result;
import com.beetles.mapper.UserMapper;
import com.beetles.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<?> getUserId(String userName) {
        Map<String,Object> info = userMapper.getUserIdByName(userName);
        Map<String, Object> result = userMapper.getUserIdByName(userName);
        result.put("id", info.get("id"));
        result.put("nickName", info.get("nick_name"));
        result.put("avatar", info.get("avatar"));
        if(info.isEmpty()){
            return new Result<>().error("用户不存在");
        }else {
            return new Result<>().success(200, "获取用户信息成功").put(result);
        }
    }

    @Override
    public Result<?> getUserDetailsInfoList(String authorId, String userId) {
        List<Map<String, Object>> result = userMapper.getUserDetailsInfoList(authorId, userId);
        if(result.isEmpty()){
            return new Result<>().error("用户不存在");
        }else {
            return new Result<>().success(200, "获取用户信息成功").put(result);
        }
    }

    @Override
    public Result<?> getUserShortInfo(String userId) {
        // 总访问量 原创 粉丝 关注 || 点赞 评论 收藏 粉丝 关注
        Map<String, Object> result = userMapper.getUserShortInfo(userId);
        if(result.isEmpty()){
            return new Result<>().error("用户不存在");
        }else {
            return new Result<>().success(200, "获取用户信息成功").put(result);
        }
    }

    @Override
    public Result<?> checkIsFollowed(String userId, String followerId) {
        if(userMapper.selectFriendship(userId, followerId) == 1){
            return new Result<>().success(200, "已经关注").put("1");
        }else {
            return new Result<>().error("没有关注").put("0");
        }
    }

    @Override
    public Result<?> addUserFollows(String userId, String followerId) {
        // 判断是否已经关注
        if(userMapper.selectFriendship(userId, followerId) == 0){
            // 没有关注，添加关注
            String id = UUID.randomUUID().toString();
            int result = userMapper.addUserFollows(id, userId, followerId);
            if(result == 0){
                return new Result<>().error("添加关注失败");
            }else {
                return new Result<>().success(200, "添加关注成功");
            }
        }else {
            return new Result<>().error("已经关注");
        }
    }

    @Override
    public Result<?> removeUserFollows(String userId, String followerId) {
        if(userMapper.selectFriendship(userId, followerId) == 1){
            // 已经关注，取消关注
            int result = userMapper.removeUserFollows(userId, followerId);
            if(result == 0){
                return new Result<>().error("取消关注失败");
            }else {
                return new Result<>().success(200, "取消关注成功");
            }
        }else {
            return new Result<>().error("没有关注");
        }
    }

    @Override
    public Result<?> changeUserInfo(String type, String value, String userId) {
        // userNickname:用户昵称
        // sex:性别
        // bio:简介
        // email:邮箱
        // phonenumber:电话
//        if(Objects.equals(type, "userNickname")){
//            type = "nick_name";
//        }
        int result = userMapper.changeUserInfo(type, value, userId);
        if (result == 0){
            return new Result<>().error("修改用户信息失败");
        }else {
            return new Result<>().success(200, "修改用户信息成功");
        }
    }

    @Override
    public Result<?> getUserInfoById(String userId) {
        Map<String, Object> result = userMapper.getUserInfoById(userId);
        if(result.isEmpty()){
            return new Result<>().error("用户不存在");
        }else {
            return new Result<>().success(200, "获取用户信息成功").put(result);
        }
    }
}
