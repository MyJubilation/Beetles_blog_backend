package com.beetles.mapper;

import com.beetles.DTO.Details;
import org.apache.ibatis.annotations.Insert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface DetailsMapper {
//    @Insert("INSERT INTO article(title, tags, coverImg, summary, type, visibility, content)" +
//            "VALUES (#{details.getTitle()})")
    int addDetails(Details details);

    List<Map<String, Object>> getDetailsInfoList(int start, int pageSize, String userId, String type);

    Map<String, Object> getDetailsContent(String id);

    int getDetailsInfoListTotal(String userId, String type);

    int addComment(String id, String detailsId, String userId, String comment, int status);

    List<Map<String, Object>> getCommentsList(String detailsId);

    List<String> getCommentsDanmakus(String detailsId);

    int selectLikeDetail(String detailsId, String userId);

    int deleteLikeDetail(String detailsId, String userId);

    int addLikeDetail(String detailsId, String userId, String id);

    String selectStarDetail(String detailsId, String userId);

    int deleteStarDetail(String detailsId, String userId);

    int addStarDetail(String detailsId, String userId, String id, String folderId);

    int changeLikeInArticle(String detailsId, int i);

    int changeStarInArticle(String detailsId, int i);

    int addDetailsView(String id);

    List<Map<String, Object>> getTagsFromTag();

    List<Map<String, Object>> getTagsListInfo(String id);

    List<Map<String, Object>> getTagsFromTagContent();

    List<Map<String, Object>> getTags();

    List<Map<String, Object>> selectDetailsList(int start, int pageSize, String userId, String input, String timeNaviType);

    Object selectDetailsListTotal(String userId, String input);

    List<Map<String, Object>> getFollowedDetailsInfoList(int currentPageNum, String userId);

    List<Map<String, Object>> getStarFolderContents(String userId, String folderId, int pageSize, int start);

    List<Map<String, Object>> getStarFolderList(String userId);

    Map<String, Object> getStarFolderInfo(String folderId);

    int changeStarFolderInfo(String folderId, String type, String value);

    String selectFolderIdByUserId(String userId);

    String getFolderId(String userId, String detailsId);

    int addNewStarFolder(String uuid, String userId, String folderName, String summary, Integer isVisible);
}
