package com.beetles.controller;

import com.beetles.DTO.Result;
import com.beetles.service.AIChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class AIChatController {

    @Autowired
    private AIChatService aiChatService;

    @RequestMapping("/AIchat")
    public Result<?> AIChat(@RequestBody Map<String, Object> requestBody) {
        System.out.println("AIChat");
        List<Map<String, String>> messageOrigin = (List<Map<String, String>>) requestBody.get("messages");
        Map<String, String> message = messageOrigin.get(messageOrigin.size() - 1);
        String model = (String) requestBody.get("model");
//        System.out.println(message);
//        System.out.println(message.get("content"));
//        System.out.println(model);
        return aiChatService.AIChat(messageOrigin, model);
    }
}
