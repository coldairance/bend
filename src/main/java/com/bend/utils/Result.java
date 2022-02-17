package com.bend.utils;

import lombok.Data;

import java.util.HashMap;

@Data
public class Result {


    // 是否成功
    private Boolean success;


    /**
     * 200 正常
     * 403 未登录
     * 404 登录失败
     * 500 服务器异常
     */
    private int code;

    // 返回消息
    private String message;

    // 返回数据
    private HashMap<String, Object> data;

    public Result() {
        data = new HashMap<>();
    }


    public Result ok() {
        this.success = true;
        this.code = ResultCode.SUCCESS.getCode();
        this.message = "成功";
        return this;
    }


    public Result error() {
        this.success = false;
        this.code = ResultCode.COMMON_FAIL.getCode();
        this.message = "未知错误";
        return this;
    }

    public Result error(ResultCode code) {
        this.success = false;
        this.code = code.getCode();
        this.message = code.getMessage();
        return this;
    }


    public Result add(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}
