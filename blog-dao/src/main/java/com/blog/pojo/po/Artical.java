package com.blog.pojo.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class Artical implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文章ID
     */
    private Integer articalID;

    /**
     * 用户ID
     */
    private Integer userID;

    /**
     * 文章路径
     */
    private String mdPath;

    /**
     * 文章状态
     */
    private Boolean status;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章简介
     */
    private String description;

    /**
     * 文章浏览量
     */
    private Long view;

    /**
     * 文章标签
     */
    private String type;

    /**
     * 文章专题
     */
    private Integer specialID;

    /**
     * 文章创建时间
     */
    private LocalDateTime createTime;

    /**
     * 文章更改时间
     */
    private LocalDateTime modifyTime;
}
