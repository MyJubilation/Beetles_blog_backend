package com.beetles.controller;

import com.beetles.DTO.Details;
import com.beetles.DTO.Result;
import com.beetles.service.DetailsHistoryService;
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
    @Autowired
    private DetailsHistoryService detailsHistoryService;

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
        String type = (String) data.get("type");

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

        return detailsService.getDetailsInfoList(currentPage, pageSize, userId, type);
    }

    /**
     * 获取顶部标签
     */
    @RequestMapping("/getTags")
    public Result<?> getTags() {
        return detailsService.getTags();
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
//        details.setTags((List<String>) requestBody.get("tags"));
        List<String> tags = (List<String>) requestBody.get("tags");
        details.setCoverImg((String) requestBody.get("coverImg"));
        details.setSummary((String) requestBody.get("summary"));
        details.setType((String) requestBody.get("type"));
        details.setVisibility((String) requestBody.get("visibility"));
        details.setContent((String) requestBody.get("content"));
        details.setUserId((String) requestBody.get("userId"));

        if (detailsService.addDetails(details, tags) != 0) {
            return new Result<>().success(200, "发送详情信息成功");
        }

        return new Result<>().success(200, "发送详情信息成功");
    }

    /**
     * 添加评论
     * @param requestBody
     * @return
     */
    @RequestMapping("/addComment")
    public Result<?> addComment(@RequestBody Map<String, Object> requestBody) {
        String detailsId = (String) requestBody.get("detailsId");
        String userId = (String) requestBody.get("userId");
        String comment = (String) requestBody.get("comment");

        return detailsService.addComment(detailsId, userId, comment);
    }

    /**
     * 获取评论列表
     */
    @RequestMapping("/getCommentsList")
    public Result<?> getCommentsList(@RequestBody Map<String, Object> requestBody) {
        String detailsId = (String) requestBody.get("detailsId");
        return detailsService.getCommentsList(detailsId);
    }

    /**
     * 获取弹幕数据
     */
    @RequestMapping("/getCommentsDanmakus")
    public Result<?> getCommentsDanmakus(@RequestBody Map<String, Object> requestBody) {
        String detailsId = (String) requestBody.get("detailsId");
        return detailsService.getCommentsDanmakus(detailsId);
    }

    /**
     * 切换点赞状态
     */
    @RequestMapping("/likeDetail")
    public Result<?> likeDetail(@RequestBody Map<String, Object> requestBody) {
        String detailsId = (String) requestBody.get("detailsId");
        String userId = (String) requestBody.get("userId");
        return detailsService.likeDetail(detailsId, userId);
    }

    /**
     * 切换收藏状态
     */
    @RequestMapping("/starDetail")
    public Result<?> starDetail(@RequestBody Map<String, Object> requestBody) {
        String detailsId = (String) requestBody.get("detailsId");
        String userId = (String) requestBody.get("userId");
        return detailsService.starDetail(detailsId, userId);
    }

    /**
     * 获取当前用户是否点赞和收藏
     */
    @RequestMapping("/checkIslikeANDIsStar")
    public Result<?> checkIslikeANDIsStar(@RequestBody Map<String, Object> requestBody) {
        String detailsId = (String) requestBody.get("detailsId");
        String userId = (String) requestBody.get("userId");
        return detailsService.checkIslikeANDIsStar(detailsId, userId);
    }

    /**
     * 添加历史记录 TODO
     */
    @RequestMapping("/addHistory")
    public Result<?> addHistory(@RequestBody Map<String, Object> requestBody) {
        String userId = (String) requestBody.get("userId");
        String articleId = (String) requestBody.get("articleId");
        String type = (String) requestBody.get("type");
        return detailsHistoryService.addToHistory(userId, articleId, type);
    }
    /**
     * 获取历史记录
     * TODO 最多每人20条数据,去掉Page个size
     */
    @RequestMapping("/getHistory")
    public Result<?> getHistory(@RequestBody Map<String, Object> requestBody) {
        String userId = (String) requestBody.get("userId");
        String type = (String) requestBody.get("type");
        return detailsHistoryService.getHistory(userId, type);
    }
    /**
     * 删除历史记录
     */
    @RequestMapping("/removeHistoryItem")
    public Result<?> removeHistoryItem(@RequestBody Map<String, Object> requestBody) {
        String userId = (String) requestBody.get("userId");
        String articleId = (String) requestBody.get("articleId");
        return detailsHistoryService.removeHistoryItem(userId, articleId);
    }
    /**
     * 清空历史记录
     */
    @RequestMapping("/clearHistory")
    public Result<?> clearHistory(@RequestBody Map<String, Object> requestBody) {
        String userId = (String) requestBody.get("userId");
        return detailsHistoryService.clearHistory(userId);
    }

    /**
     * 创作界面获取标签类型列表
     * return: 标签类别，子标签列表
     */
    @RequestMapping("/getStatusList")
    public Result<?> getStatusList() {
        return detailsService.getStatusList();
    }

    /**
     * 获取底部标签数据
     */
    @RequestMapping("/getTagList")
    public Result<?> getTagList() {
        return detailsService.getTagsList();
    }

}
