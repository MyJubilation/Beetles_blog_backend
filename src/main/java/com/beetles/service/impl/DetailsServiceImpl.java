package com.beetles.service.impl;

import com.beetles.DTO.Details;
import com.beetles.DTO.Result;
import com.beetles.mapper.DetailsMapper;
import com.beetles.service.DetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DetailsServiceImpl implements DetailsService {

    @Autowired
    private DetailsMapper detailsMapper;

    @Override
    public Result<?> getDetailsInfoList(int currentPage, int pageSize, String userId) {
        // 处理数据
        int start = (currentPage - 1) * pageSize;
        Map<String, Object> result = new HashMap<>();
        // 查询当前页数据
        List<Map<String,Object>> listInfo = detailsMapper.getDetailsInfoList(start, pageSize, userId);
        result.put("listInfo", listInfo);
        // 查询总条数
        int total = detailsMapper.getDetailsInfoListTotal(userId);
        result.put("total", total);
        if(result != null){
            return new Result<>().put(result).success(200, "获取详情信息列表成功");
        }else {
            return new Result<>().error("获取详情信息列表失败");
        }
    }

    @Override
    public Result<?> getDetailsContent(String id) {
        Map<String, Object> map = detailsMapper.getDetailsContent(id);
        if (map == null) {
            return new Result<>().error( "获取文章内容失败");
        } else {
            return new Result<>().success(200, "获取文章内容成功").put(map);
        }
    }

    @Override
    public int addDetails(Details details) {
        return detailsMapper.addDetails(details);
    }
}
