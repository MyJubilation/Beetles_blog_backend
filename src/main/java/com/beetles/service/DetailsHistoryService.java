package com.beetles.service;

import com.beetles.DTO.Result;

import java.util.List;
import java.util.Map;

public interface DetailsHistoryService {
    Result<?> addToHistory(String userId, String articleId, String type);
    Result<?> getHistory(String userId, String type);
    Result<?> removeHistoryItem(String userId, String articleId);
    Result<?> clearHistory(String userId);
}
