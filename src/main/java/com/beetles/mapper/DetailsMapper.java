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

    List<Map<String, Object>> getDetailsInfoList(int start, int pageSize, String userId);

    Map<String, Object> getDetailsContent(String id);

    int getDetailsInfoListTotal(String userId);

    int addComment(String id, String detailsId, String userId, String comment, int status);

    List<Map<String, Object>> getCommentsList(String detailsId);

    List<String> getCommentsDanmakus(String detailsId);

    int selectLikeDetail(String detailsId, String userId);

    int deleteLikeDetail(String detailsId, String userId);

    int addLikeDetail(String detailsId, String userId, String id);

    int selectStarDetail(String detailsId, String userId);

    int deleteStarDetail(String detailsId, String userId);

    int addStarDetail(String detailsId, String userId, String id);

    int changeLikeInArticle(String detailsId, int i);

    int changeStarInArticle(String detailsId, int i);
}
