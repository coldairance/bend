package com.blog.pojo.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户密码
     */
    private String userPwd;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 用户手机
     */
    private String userPhone;

    /**
     * 用户头像链接
     */
    private String userHeader;

    /**
     * 用户背景链接
     */
    private String userBackground;

    /**
     * 注册时间
     */
    private LocalDateTime createTime;

    /**
     * 更改时间
     */
    private LocalDateTime modifyTime;

}
