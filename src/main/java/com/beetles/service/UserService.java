package com.beetles.service;

import com.beetles.DTO.Result;

public interface UserService {
    Result<?> getUserId(String userName);
}
