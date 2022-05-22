package com.blog.security.auth;

import com.blog.security.utils.JSONAuthentication;
import com.blog.security.utils.Result;
import com.blog.security.utils.ResultCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        Result result = new Result();
        result.error(ResultCode.USER_NO_PERMISSION);
        JSONAuthentication.writeJSON(httpServletRequest, httpServletResponse, result);
    }
}
