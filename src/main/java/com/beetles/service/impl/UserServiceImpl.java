package com.beetles.service.impl;

import com.beetles.DTO.Result;
import com.beetles.mapper.UserMapper;
import com.beetles.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<?> getUserId(String userName) {
        int id = userMapper.getUserIdByName(userName);
        if(id == 0){
            return new Result<>().error("用户不存在");
        }else {
            return new Result<>().success(200, "获取用户id成功").put(id);
        }
    }
}
