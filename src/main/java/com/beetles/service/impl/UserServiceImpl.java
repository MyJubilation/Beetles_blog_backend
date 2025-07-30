package com.beetles.service.impl;

import com.beetles.DTO.Result;
import com.beetles.mapper.UserMapper;
import com.beetles.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

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
}
