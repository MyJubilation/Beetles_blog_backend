package com.beetles.service.impl;

import com.beetles.DTO.Result;
import com.beetles.service.AIChatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper; // 添加Jackson依赖

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AIChatServiceImpl implements AIChatService {

    @Value("${deepseek.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper(); // 创建ObjectMapper实例

    @Override
    public Result<?> AIChat(List<Map<String, String>> message, String model) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 构建请求体
            Map<String, Object> payload = new HashMap<>();
            payload.put("model", model != null ? model : "deepseek-chat");
            payload.put("messages", message);

            // 创建请求实体
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

            // 发送请求
            String url = "https://api.deepseek.com/v1/chat/completions";
            String responseString = restTemplate.postForObject(url, entity, String.class);

            System.out.println("response:");
            System.out.println(responseString);

            // 将String响应转换为Map
            Map<String, Object> responseMap = objectMapper.readValue(responseString, Map.class);

            // 获取唯一标识id
            String id = (String) responseMap.get("id");

            // 从响应中提取所需信息
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");
            /**
             * role: "assistant",
             * content: "Deepseek回复内容"
             */
            Map<String, String> content = (Map<String, String>) choices.get(0).get("message");

            // 获取token使用情况
            Map<String, Object> usage = (Map<String, Object>) responseMap.get("usage");
            int totalTokens = (Integer) usage.get("total_tokens");

            // 创建结果Map
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("id", id);
            resultData.put("content", content);
            resultData.put("totalTokens", totalTokens);
            resultData.put("fullResponse", responseMap); // 保留完整响应

            return new Result<>().success("获取AI聊天结果成功").put(resultData);
        } catch (Exception e) {
            e.printStackTrace(); // 打印详细错误信息
            return new Result<>().error("AI聊天请求失败: " + e.getMessage());
        }
    }
}