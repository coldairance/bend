package com.blog.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private Integer articalNumber;
    private Integer specialNumber;
    private Integer viewNumber;
    private String header;
    private String background;
}
