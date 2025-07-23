package com.beetles.controller;

import com.beetles.DTO.Details;
import com.beetles.DTO.Result;
import com.beetles.service.DetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class DetailsController {

    @Autowired
    private DetailsService detailsService;

    /**
     * 在主页面获取文章信息列表
     * @param data
     * @return
     */
    @RequestMapping("/getDetailsInfoList")
    public Result<?> getDetailsInfoList(@RequestBody Map<String, Object> data) {
        int currentPage = (int) data.get("currentPage");
        int pageSize = (int) data.get("pageSize");
        String userId = (String) data.get("userId");

        // ------------------------------------ 暂时没有弄懂原理，也没有用处------------------------------------------
        // 获取登录状态
        // 1. 获取当前的安全上下文
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 2. 检查认证对象是否存在且已认证
        if (authentication != null && authentication.isAuthenticated()) {
            // 3. 获取认证主体（用户信息）
            Object principal = authentication.getPrincipal();

            // 4. 检查主体是否是 UserDetails 类型（代表已注册用户）
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                String username = userDetails.getUsername();
                // 5. 可以在这里进行其他操作，比如获取用户权限等
                // 其他操作...
            }
            // 5.1 如果主体不是 UserDetails 类型，可能是 String "anonymousUser"（匿名用户）
            //     这种情况通常在未登录但请求需要认证资源时发生
        } else {
            // 6. 处理未登录或认证失败的情况
            // 未登录处理
        }
        // ------------------------------------ 暂时没有弄懂原理，也没有用处------------------------------------------

        return detailsService.getDetailsInfoList(currentPage, pageSize, userId);
    }

    /**
     * 获取文章内容
     * @param requestBody
     * @return
     */
    @RequestMapping("/getDetailsContent")
    public Result<?> getDetailsContent(@RequestBody Map<String, Object> requestBody) {
        String id = (String) requestBody.get("detailsId");
        return detailsService.getDetailsContent(id);
    }

    @RequestMapping("/sendDetails")
    public Result<?> sendDetails(@RequestBody Map<String, Object> requestBody) {
        Details details = new Details();
        details.setId(String.valueOf(UUID.randomUUID()));
        details.setTitle((String) requestBody.get("title"));
        details.setTags((List<String>) requestBody.get("tags"));
        details.setCoverImg((String) requestBody.get("coverImg"));
        details.setSummary((String) requestBody.get("summary"));
        details.setType((String) requestBody.get("type"));
        details.setVisibility((String) requestBody.get("visibility"));
        details.setContent((String) requestBody.get("content"));
        details.setUserId((String) requestBody.get("userId"));

        if (detailsService.addDetails(details) != 0) {
            return new Result<>().success(200, "发送详情信息成功");
        }

        return new Result<>().success(200, "发送详情信息成功");
    }
}
