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
}
