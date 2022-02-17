package com.bend.utils;

public enum ResultCode {

    /* 默认成功 */
    SUCCESS(200, "成功"),

    /* 默认失败 */
    COMMON_FAIL(-1, "失败"),

    /**
     * 登录异常
     */
    USER_ACCOUNT_NOT_EXIST(1000, "账号不存在"),
    USER_CREDENTIALS_ERROR(1001, "密码错误"),
    USER_NOT_LOGIN(1002, "用户未登录"),


    /**
     * 可忽略异常
     */
    /*文件处理*/
    UPLOAD_FAILURE(2001, "文件上传失败"),
    FILE_GET_FAILURE(2002, "文章拉取失败"),
    UPDATE_FILE_FAILURE(2003, "文章修改失败"),
    FILE_NOT_FOUND(2004, "文章不存在");



    private Integer code;

    private String message;

    ResultCode(Integer code,String message){
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
