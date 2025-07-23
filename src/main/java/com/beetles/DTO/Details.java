package com.beetles.DTO;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Details {
    private String id;
    private String title; // 标题
    private List<String> tags; // 标签
    private String coverImg; // 封面图片
    private String summary; // 摘要
    private String type; // 类型 (1:原创; 2:转载)
    private String visibility; // 可见性(1:全部可见; 2:仅我可见; 3:粉丝可见)
    private String content; // 内容
    private String userId; // 作者ID
    private Date publishTime; // 发布时间
    private Date createTime; // 创建时间
    private Date updateTime; // 更新时间
}
