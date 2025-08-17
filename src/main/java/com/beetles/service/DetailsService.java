package com.beetles.service;

import com.beetles.DTO.Details;
import com.beetles.DTO.Result;

import java.util.List;

public interface DetailsService {
    int addDetails(Details details, List<String> tags);

    Result<?> getDetailsInfoList(int currentPage, int pageSize, String userId, String type);

    Result<?> getDetailsContent(String id);

    Result<?> addComment(String detailsId, String userId, String comment);

    Result<?> getCommentsList(String detailsId);

    Result<?> getCommentsDanmakus(String detailsId);

    Result<?> likeDetail(String detailsId, String userId);

    Result<?> checkIslikeANDIsStar(String detailsId, String userId);

    Result<?> starDetail(String detailsId, String userId);

    Result<?> getTags();

    Result<?> getStatusList();

    Result<?> getTagsList();
}
