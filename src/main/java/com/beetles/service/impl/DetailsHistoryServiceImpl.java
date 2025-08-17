package com.beetles.service.impl;

import com.beetles.DTO.Result;
import com.beetles.mapper.DetailsMapper;
import com.beetles.service.DetailsHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class DetailsHistoryServiceImpl implements DetailsHistoryService {

    @Autowired
    private DetailsMapper detailsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String HISTORY_KEY = "user:history:";
//    private static final Integer HISTORY_SIZE = 20; // 最大历史记录数值
    private static final int MAX_HISTORY_SIZE = 20; // 最大保存历史记录数

    /**
     * 添加文章到观看历史
     */
    public Result<?> addToHistory(String userId, String articleId, String type) {

        String key = HISTORY_KEY + userId + ":" + type;  // user:history:3:details
        List<Object> historyList = redisTemplate.opsForList().range(key, 0, -1); // 获取 Redis 中指定键名对应的完整列表数据，返回一个包含所有元素的集合

        boolean found = false;
        Map<String, Object> updatedItem = null;

//        // 检查是否已经达到了最大历史记录数
//        // 如果达到了，则删除最旧的记录
//        // 达到20条时，删除到剩19条，多的一条用于新增该历史记录数据
//        if (historyList.size() >= 20) {
//            // 计算需要删除的记录数
//            int removeCount = historyList.size() - 19;
//
//            // 删除最旧的记录（列表左侧的记录）
//            redisTemplate.opsForList().trim(key, removeCount, -1);
//        }

        // 遍历查找是否已存在该文章
        for (Object item : historyList) {
            Map<String, Object> historyItem = (Map<String, Object>) item;
            if (articleId.equals(historyItem.get("articleId"))) {
                // 存在则删除旧记录，重新插入到头部（模拟更新）
                redisTemplate.opsForList().remove(key, 1, historyItem);
                historyItem.put("viewTime", System.currentTimeMillis()); // 更新时间
                updatedItem = historyItem;
                redisTemplate.opsForList().leftPush(key, updatedItem);
                found = true;
                break;
            }
        }

        if (!found) {
            // 不存在则新增
            Map<String, Object> newItem = new HashMap<>();
            newItem.put("articleId", articleId);
            newItem.put("viewTime", System.currentTimeMillis());
//            newItem.put("type", type);
            redisTemplate.opsForList().leftPush(key, newItem);
        }

        // 限制历史记录数量
        redisTemplate.opsForList().trim(key, 0, MAX_HISTORY_SIZE - 1);

        // 设置过期时间（例如30天）
        redisTemplate.expire(key, 30, TimeUnit.DAYS);
        return new Result<>().success(200, "添加历史记录成功");
    }

    /**
     * 获取用户观看历史
     */
    public Result<?> getHistory(String userId, String type) {


        String key = HISTORY_KEY + userId + ":" + type;

        List<Object> historyList = redisTemplate.opsForList().range(key, 0, -1);

        List<Map<String, Object>> result = new ArrayList<>();
        // 处理数据
        for(Object item : historyList){
            // 获取当前条的redis信息
            Map<String, Object> historyItem = (Map<String, Object>) item;
            // 定义返回结果数据Map
            Map<String, Object> historyListResult = new HashMap<>();
            // 将viewTime序列化
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long viewTime = (Long) historyItem.get("viewTime");
            String formattedTime = sdf.format(new Date(viewTime));
            historyListResult.put("viewTime", formattedTime);
            // 根据articleId查询数据:detailsId, title, authorNickname,avatar
            Map<String, Object> details = detailsMapper.getDetailsContent(historyItem.get("articleId").toString());
            historyListResult.put("details", details);
            // 将结果存储到result中
            result.add(historyListResult);
        }
        return new Result<>().success(200, "获取历史记录成功").put(result);
    }

    /**
     * 删除单条历史记录
     */
    public Result<?> removeHistoryItem(String userId, String articleId) {
        String key = HISTORY_KEY + userId;
        List<Object> historyList = redisTemplate.opsForList().range(key, 0, -1);

        for (int i = 0; i < historyList.size(); i++) {
            Map<String, Object> item = (Map<String, Object>) historyList.get(i);
            if (articleId.equals(item.get("articleId"))) {
                redisTemplate.opsForList().remove(key, 1, item);
                break;
            }
        }
        return new Result<>().success(200, "删除历史记录成功");
    }

    /**
     * 清空用户历史记录
     */
    public Result<?> clearHistory(String userId) {
        String key = HISTORY_KEY + userId;
        redisTemplate.delete(key);
        return new Result<>().success(200, "清空历史记录成功");
    }

}
