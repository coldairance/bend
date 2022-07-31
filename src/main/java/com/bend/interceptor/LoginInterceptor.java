package com.bend.interceptor;

import cn.hutool.json.JSONUtil;
import com.bend.utils.Result;
import com.bend.utils.ResultCode;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        Object login = session.getAttribute("login");
        if(login == null) {
            Result result = new Result();
            result.error(ResultCode.USER_NOT_LOGIN);
            response.getWriter().write(JSONUtil.toJsonStr(result));
            return false;
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
