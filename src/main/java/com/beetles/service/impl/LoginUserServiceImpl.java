package com.beetles.service.impl;

import com.beetles.DTO.LoginUser;
import com.beetles.DTO.Result;
import com.beetles.DTO.User;
import com.beetles.mapper.UserMapper;
import com.beetles.service.LoginService;
import com.beetles.utils.JwtUtil;
import com.beetles.utils.RedisCache;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;


@Service
public class LoginUserServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private UserMapper userMapper;
    @Resource
    private PasswordEncoder passwordEncoder;

    public Result<?> login(User user){

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        if(Objects.isNull(authentication)){
//            throw new RuntimeException("用户名或密码错误");
            return new Result<>().error("用户名或密码错误");
        }

        //使用userid生成token
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);

        //authenticate存入redis
        redisCache.setCacheObject("login:"+userId,loginUser);
        //把token响应给前端
        HashMap<String,String> map = new HashMap<>();
        map.put("token",jwt);
        // 获取用户type
        int type = userMapper.getUserTypeByUserId(userId);
        map.put("type", String.valueOf(type));
        return new Result<>().success(200,"登陆成功").put(map);
    }

    @Override
    public Result<?> logout() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();
        redisCache.deleteObject("login:"+userid);
        return new Result<>().success(200, "退出成功");
    }

    @Override
    public Result<?> register(User user) {
        // 用户注册相关逻辑
        /**
         * userName 用户名
         * password 密码
         * nickName 昵称
         * * email 邮箱
         * * phonenumber 手机号
         * * sex
         */

        // 查重：id；nickName；userName
        if(userMapper.getUserName(user.getUserName())){
            return new Result<>().error("用户名已存在");
        }
        if(userMapper.getNickName(user.getNickName())){
            return new Result<>().error("昵称已存在");
        }

        // 设置初始值
        Random random = new Random();
        // 随机生成id并查询id是否重复
        do {
            user.setId((long) (100000 + random.nextInt(900000)));
        } while (userMapper.getUserId(user.getId()));
        user.setStatus("0"); // 账号状态（0正常 1停用）
        user.setSex(user.getSex() != null && (user.getSex().equals("0") || user.getSex().equals("1")) ? user.getSex() : "2"); // 用户性别（0男，1女，2未知）
        user.setUserType("1"); // 用户类型（0管理员，1普通用户）
        Date now = new Date();
        user.setCreateTime(now);
        user.setUpdateTime(now);
        user.setDelFlag(0); // 删除标志（0代表未删除，1代表已删除）
        user.setPassword(passwordEncoder.encode(user.getPassword())); // 对密码进行加密

        int result = userMapper.register(user);

        if(result == 0){
            return new Result<>().error("注册失败");
        }else {
            return new Result<>().success(200, "注册成功");
        }

    }
}
