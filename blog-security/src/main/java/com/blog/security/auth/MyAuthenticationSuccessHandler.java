package com.blog.security.auth;

import com.blog.mapper.UserMapper;
import com.blog.pojo.po.User;
import com.blog.pojo.vo.UserInfo;
import com.blog.security.utils.JSONAuthentication;
import com.blog.security.utils.JWTUtils;
import com.blog.security.utils.Result;
import com.blog.security.utils.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;;
import java.util.*;


/**
 * 登录成功
 */
@Component("myAuthenticationSuccessHandler")
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserMapper userMapper;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        // 只需要以json的格式返回一个提示
        // Result类的json数据

        HashMap<String, String> map = new HashMap<>();
        String username = httpServletRequest.getParameter("username");
        Date date = new Date();
        map.put("username", username);
        map.put("login-time", date.getTime()+"");

        // 获得token
        String token = JWTUtils.getToken(map);

        User user = userMapper.selectUserByUserName(username);

        // 查询用户的权限集合
        List<GrantedAuthority> authorities = new ArrayList<>();


        List<String> auths = userMapper.selectAuthoritiesByUsername(username);

        // 保存权限信息
        auths.forEach(auth -> {
            authorities.add(new SimpleGrantedAuthority(auth));
        });

        Result result = new Result();

        result.data("token",token)
                .data("user", user)
                .data("authorities", auths);
        result.ok();

        // 存入Redis
        Token.set(username, token);

        JSONAuthentication.writeJSON(httpServletRequest, httpServletResponse, result);
    }
}
