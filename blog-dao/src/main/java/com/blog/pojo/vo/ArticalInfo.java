package com.blog.pojo.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class ArticalInfo implements Serializable {

    private String username;
    private String header;
    private Integer articalID;
    private String title;
    private String description;
    private Integer status;
    private LocalDateTime lastModifyTime;
    private String type;
    private Integer specialID;
    private String specialTitle;
}
