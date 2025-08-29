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

    Result<?> starDetail(String detailsId, String userId, String folderId);

    Result<?> getTags();

    Result<?> getStatusList();

    Result<?> getTagsList();

    Result<?> selectDetailsList(int currentPage, int pageSize, String userId, String input, String timeNaviType);

    Result<?> getFollowedDetailsInfoList(int currentPageNum, String userId);

    Result<?> getStarFolderContents(String userId, String folderId, int pageSize, int currentPage);

    Result<?> getStarFolderList(String userId);

    Result<?> changeStarFolderInfo(String folderId, String type, String value);

    Result<?> getFolderId(String userId, String detailsId);

    Result<?> addNewStarFolder(String userId, String folderName, String summary, Integer isVisible);
}
