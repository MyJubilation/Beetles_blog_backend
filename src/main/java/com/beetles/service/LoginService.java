package com.beetles.service;

import com.beetles.DTO.Result;
import com.beetles.DTO.User;

public interface LoginService {
    Result<?> login(User user);
    Result<?> logout();

    Result<?> register(User user);
}
