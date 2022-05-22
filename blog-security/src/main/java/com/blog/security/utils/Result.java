package com.blog.security.utils;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Result {

    // 是否成功
    private Boolean success;

    // 返回码
    private Integer code;

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
        this.message = ResultCode.SUCCESS.getMessage();
        return this;
    }


    public Result error() {
        this.success = false;
        this.code = ResultCode.COMMON_FAIL.getCode();
        this.message = ResultCode.COMMON_FAIL.getMessage();
        return this;
    }

    public Result error(String message) {
        this.success = false;
        this.code = ResultCode.COMMON_FAIL.getCode();
        this.message = message;
        return this;
    }

    public Result error(ResultCode resultCode){
        this.success = false;
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        return this;
    }

    /**
     * 自定义返回成功与否
     */
    public Result success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public Result message(String message){
        this.setMessage(message);
        return this;
    }

    public Result code(Integer code){
        this.setCode(code);
        return this;
    }

  public Result data(String key, Object value) {
    this.data.put(key, value);
    return this;
  }
}
