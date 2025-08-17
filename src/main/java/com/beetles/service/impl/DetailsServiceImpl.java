package com.beetles.service.impl;

import com.beetles.DTO.Details;
import com.beetles.DTO.Result;
import com.beetles.mapper.DetailsMapper;
import com.beetles.service.DetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DetailsServiceImpl implements DetailsService {

    @Autowired
    private DetailsMapper detailsMapper;

    @Override
    public Result<?> getDetailsInfoList(int currentPage, int pageSize, String userId, String type) {
        // 处理数据
        int start = (currentPage - 1) * pageSize;
        Map<String, Object> result = new HashMap<>();
        if(Objects.equals(type, "all")){
            type = null;
        }else {
//            type = '"' + type + '"';
        }
        // 查询当前页数据
        List<Map<String,Object>> listInfo = detailsMapper.getDetailsInfoList(start, pageSize, userId, type);
        result.put("listInfo", listInfo);
        // 查询总条数
        int total = detailsMapper.getDetailsInfoListTotal(userId, type);
        result.put("total", total);
        if(result != null){
            return new Result<>().put(result).success(200, "获取详情信息列表成功");
        }else {
            return new Result<>().error("获取详情信息列表失败");
        }
    }

    @Override
    public Result<?> getTags() {
        List<Map<String, Object>> tags = detailsMapper.getTags();
        if (tags != null){
            return new Result<>().success(200, "获取标签成功").put(tags);
        }else {
            return new Result<>().error("获取标签失败");
        }
    }

    @Override
    public Result<?> getDetailsContent(String id) {
        int result = detailsMapper.addDetailsView(id);
        Map<String, Object> map = detailsMapper.getDetailsContent(id);
        if (map == null) {
            return new Result<>().error( "获取文章内容失败");
        } else {
            return new Result<>().success(200, "获取文章内容成功").put(map);
        }
    }

    @Override
    public int addDetails(Details details,List<String> tagsParams) {
        // 处理数据
        // 处理tags
        // params:["python:python","Java:java"]
        List<Map<String, Object>> tags = new ArrayList<>();
        List<String> tagsRaw = tagsParams;
        for (String params : tagsRaw){
            String[] parts = params.split(":", 2); // 限制分割次数为2

            if (parts.length == 2) {
                String tag = parts[0];  // "Java"
                String content = parts[1];  // "Springboot"
                // 判断当前tag是否存在，并获取对应Map
                Map<String, Object> javaMap = tags.stream()
                        .filter(map -> tag.equals(map.get("tag")))
                        .findFirst()
                        .orElse(null);
                if (javaMap != null) {
                    // 已经存在当前tag的Map
                    String contentList = (String) javaMap.get("content");
                    contentList = contentList.substring(0, contentList.length() - 2);
                    contentList += ",\""+content+"\"]}";
                    javaMap.put("content", contentList);
                } else {
                    // 不存在当前tag的Map，新建
                    Map<String, Object> map = new HashMap<>();
                    map.put("tag", "{\""+tag+"\"");
                    map.put("content", "[\"" +content+"\"]}");
                    tags.add(map);
                }
            }
        }

        List<String> currentTags = new ArrayList<>();
        for (Map<String, Object> map : tags){
            Map<String, Object> mapTemp = new HashMap<>();
            mapTemp.put((String) map.get("tag"), map.get("content"));
            // mapTemp:{Python=["python","django","tornado","scikit-learn"]}

            // 将Map转换为String，并将=替换为:
            String result = mapTemp.entrySet()
                    .stream()
                    .map(entry -> entry.getKey() + ":" + entry.getValue())
                    .collect(Collectors.joining(", "));
            currentTags.add(result);
        }
        // currentTags:[{Python=["python","django","tornado","scikit-learn"]}, {Java=["java","maven"]}]
        String currentTagsString = currentTags.toString();
//        System.out.println(currentTagsString);
        details.setTags(currentTagsString);

        return detailsMapper.addDetails(details);
    }

    @Override
    public Result<?> addComment(String detailsId, String userId, String comment) {
        // 设置默认值
        int status = 0; // 0:待审核,1:已通过,2:已拒绝
        String id = UUID.randomUUID().toString();


        int result = detailsMapper.addComment(id, detailsId, userId, comment, status);
        return new Result<>().success(200, "添加评论成功");
    }

    @Override
    public Result<?> getCommentsList(String detailsId) {
        List<Map<String, Object>> list = detailsMapper.getCommentsList(detailsId);
        if (list != null){
            return new Result<>().success(200, "获取评论列表成功").put(list);
        }else {
            return new Result<>().error("获取评论列表失败");
        }
    }

    @Override
    public Result<?> getCommentsDanmakus(String detailsId) {
        // 获取弹幕的列表
        List<String> danmakus = detailsMapper.getCommentsDanmakus(detailsId);
        // 封装弹幕
        List<Map<String, Object>> result = new ArrayList<>();
        /**
         * 前端需求格式：
         * { text: '已经点赞收藏了', left: 0, top: 350, duration: 3 },
         * 范围：top:[ 100, 500 ] , duration: [ 4, 14 ]
         */
        for (String danmaku : danmakus) {
            // 设置left，top，duration属性
            int left = 0;
            int top = (int) (Math.random() * 400 + 100);
            int duration = (int) (Math.random() * 10 + 4);
            // 添加到result集合中
            Map<String, Object> map = new HashMap<>();
            map.put("text", danmaku);
            map.put("left", left);
            map.put("top", top);
            map.put("duration", duration);
            result.add(map);
        }
        if(result != null){
            return new Result<>().success(200, "获取弹幕列表成功").put(result);
        }else {
            return new Result<>().error("获取弹幕列表失败");
        }
    }

    @Override
    public Result<?> likeDetail(String detailsId, String userId) {
        // 查询是否已经点赞
        int count = detailsMapper.selectLikeDetail(detailsId, userId);
        int result = 0;
        int status = 0;
        if(count == 1){
            // 已经点赞，进行取消点赞功能
            result = detailsMapper.deleteLikeDetail(detailsId, userId);
            // 在article表中更新点赞记录
            result = detailsMapper.changeLikeInArticle(detailsId, -1);
            status = -1;
        }else {
            // 未点赞，进行点赞功能
            String id = UUID.randomUUID().toString();
            result = detailsMapper.addLikeDetail(detailsId, userId, id);
            // 在article表中更新点赞记录
            result = detailsMapper.changeLikeInArticle(detailsId, 1);
            status = 1;
        }
        if(result == 1){
            return new Result<>().success(200, "点赞成功").put(status);
        }else {
            return new Result<>().error("点赞失败");
        }
    }

    @Override
    public Result<?> starDetail(String detailsId, String userId) {
        // 查询是否已经收藏
        int count = detailsMapper.selectStarDetail(detailsId, userId);
        int result = 0;
        int status = 0;
        if(count == 1){
            // 已经收藏，进行取消收藏功能
            result = detailsMapper.deleteStarDetail(detailsId, userId);
            // 在article表中更新收藏记录
            result = detailsMapper.changeStarInArticle(detailsId, -1);
            status = -1;
        }else {
            // 未收藏，进行收藏功能
            String id = UUID.randomUUID().toString();
            result = detailsMapper.addStarDetail(detailsId, userId, id);
            // 在article表中更新收藏记录
            result = detailsMapper.changeStarInArticle(detailsId, 1);
            status = 1;
        }
        if(result == 1){
            return new Result<>().success(200, "收藏成功").put(status);
        }else {
            return new Result<>().error("收藏失败");
        }
    }

    @Override
    public Result<?> checkIslikeANDIsStar(String detailsId, String userId) {
        // 查询是否已经点赞和收藏
        int isLike = detailsMapper.selectLikeDetail(detailsId, userId);
        int isStar = detailsMapper.selectStarDetail(detailsId, userId);
        Map<String, Object> result = new HashMap<>();
        result.put("isLike", isLike);
        result.put("isStar", isStar);
        if(result != null){
            return new Result<>().success(200, "查询是否点赞和收藏成功").put(result);
        }else {
            return new Result<>().error("查询是否点赞和收藏失败");
        }
    }

    @Override
    public Result<?> getStatusList() {
        // 返回类型：[{"content": "Python", "tag": "Python", "value": [{"content": "python", "name": "python"},{...}]}, ]
        // 获取主标签列表
        List<Map<String, Object>> typeListTag = detailsMapper.getTagsFromTag();
        // 获取子标签列表
        List<Map<String, Object>> typeListContent = detailsMapper.getTagsFromTagContent();
        // 合并
        List<Map<String, Object> > typeList = new ArrayList<>();
        typeList.addAll(typeListTag);
        typeList.addAll(typeListContent);

        // 查询标签类别列表
        // 删除全部标签
        typeList.removeIf(map -> "all".equals(map.get("content")));

        // 返回
        if(typeList != null){
            return new Result<>().success(200, "获取标签列表成功").put(typeList);
        }else {
            return new Result<>().error("获取标签列表失败");
        }
    }

    @Override
    public Result<?> getTagsList() {
        // 获取主标签列表
        List<Map<String, Object>> typeList = detailsMapper.getTags();
        // 删除全部标签
        typeList.removeIf(map -> "all".equals(map.get("name")));
        // TODO 添加推荐标签
        // 查询子标签
        for (Map<String, Object> type : typeList){
            String id = (String) type.get("id");
            String name = (String) type.get("name");
            List<Map<String, Object>> tagList = detailsMapper.getTagsListInfo(id);
            type.put("content", tagList);
        }
        // 返回
        if(typeList != null){
            return new Result<>().success(200, "获取标签列表成功").put(typeList);
        }else {
            return new Result<>().error("获取标签列表失败");
        }
    }

}
