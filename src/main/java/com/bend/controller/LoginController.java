package com.bend.controller;

import cn.hutool.http.server.HttpServerRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bend.dao.UserMapper;
import com.bend.entity.User;
import com.bend.utils.Result;
import com.bend.utils.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
public class LoginController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/logining")
    public Result logining(
            HttpSession session
    ) {
        Result result = new Result();
        result.ok();

        return result;
    }

    @PostMapping("/login")
    public Result login(
            HttpServerRequest request,
            HttpServletResponse response,
            HttpSession session,
            @RequestBody String info
    ) {
        JSONObject jsonObject = JSONUtil.parseObj(info);
        String username = jsonObject.getStr("username");
        String password = jsonObject.getStr("password");

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));

        Result result = new Result();

        if(user == null) {
            result.error(ResultCode.USER_ACCOUNT_NOT_EXIST);
        }else if(!user.getPassword() .equals(password)) {
            result.error(ResultCode.USER_CREDENTIALS_ERROR);
        }else {
            result.ok();
            session.setAttribute("login",true);
            session.setAttribute("current",user);
            result.ok();
        }

        return result;
    }
}
