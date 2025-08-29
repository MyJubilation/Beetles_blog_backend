package com.beetles.service;

import com.beetles.DTO.Result;

public interface UserService {
    Result<?> getUserId(String userName);

    Result<?> getUserDetailsInfoList(String authorId, String userId);

    Result<?> getUserShortInfo(String userId);

    Result<?> addUserFollows(String userId, String followerId);

    Result<?> checkIsFollowed(String userId, String followerId);

    Result<?> removeUserFollows(String userId, String followerId);

    Result<?> changeUserInfo(String type, String value, String userId);

    Result<?> getUserInfoById(String userId);
}
