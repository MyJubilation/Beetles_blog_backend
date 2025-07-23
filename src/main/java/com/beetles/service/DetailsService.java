package com.beetles.service;

import com.beetles.DTO.Details;
import com.beetles.DTO.Result;

public interface DetailsService {
    int addDetails(Details details);

    Result<?> getDetailsInfoList(int currentPage, int pageSize, String userId);

    Result<?> getDetailsContent(String id);
}
