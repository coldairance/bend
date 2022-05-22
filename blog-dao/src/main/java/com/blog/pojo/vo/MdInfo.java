package com.blog.pojo.vo;

import lombok.Data;

@Data
public class MdInfo {
    private String path;
    private String title;
    private String description;
    private Boolean status;
    private Long view;
    private String username;
    private String type;
    private String specialTitle;
}
