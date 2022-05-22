package com.blog.security.utils;

public enum ResultCode implements CustomizeResultCode{

    /* 默认成功 */
    SUCCESS(200, "成功"),

    /* 默认失败 */
    COMMON_FAIL(999, "失败"),

    /**
     * 登录异常
     */
    USER_ACCOUNT_NOT_EXIST(1000, "账号不存在"),
    USER_CREDENTIALS_ERROR(1001, "密码错误"),
    USER_NOT_LOGIN(1002, "用户未登录"),

    /**
     * 不可忽略异常
     */
    USER_ACCOUNT_EXPIRED(2000, "验证信息已过期"),
    USER_ACCOUNT_USE_BY_OTHERS(2001, "您的账号已经在另一台机器登录，您被迫下线"),
    USER_NO_PERMISSION(2001, "没有权限"),
    /*系统攻击*/
    SYSTEM_UNDER_ATTRACK(2002, "系统被攻击，您的账号出现异常，请重新登录"),


    /**
     * 可忽略异常
     */
    /* 文章状态错误 */
    ARTICAL_NOT_PUBLISH(3000, "文章未发布"),
    /*文件处理*/
    UPLOAD_FAILURE(3001, "文件上传失败"),
    FILE_GET_FAILURE(3002, "文章拉取失败"),
    UPDATE_FILE_FAILURE(3003, "文章修改失败"),
    FILE_NOT_FOUND(3004, "文章不存在");

    /* 专题状态错误 */


    private Integer code;

    private String message;

    ResultCode(Integer code,String message){
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
