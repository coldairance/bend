package com.blog.security.auth;

import com.blog.security.utils.JSONAuthentication;
import com.blog.security.utils.Result;
import com.blog.security.utils.ResultCode;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("myAuthenticationFailureHandler")
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        // 只需要以json的格式返回一个提示
        // Result类的json数据
        Result failure = new Result();
        if (e instanceof BadCredentialsException) {
            //密码错误
            failure.error(ResultCode.USER_CREDENTIALS_ERROR);
        } else if (e instanceof InternalAuthenticationServiceException) {
            //用户不存在
            failure.error(ResultCode.USER_ACCOUNT_NOT_EXIST);
        }else{
            //其他错误
            failure.error(ResultCode.COMMON_FAIL);
        }

        JSONAuthentication.writeJSON(httpServletRequest, httpServletResponse, failure);
    }
}

