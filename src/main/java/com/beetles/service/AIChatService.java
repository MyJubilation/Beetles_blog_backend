package com.beetles.service;

import com.beetles.DTO.Result;

import java.util.List;
import java.util.Map;

public interface AIChatService {
    Result<?> AIChat(List<Map<String, String>> message, String model);
}
