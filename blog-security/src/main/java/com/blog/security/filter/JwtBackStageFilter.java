package com.blog.security.filter;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.blog.mapper.UserMapper;
import com.blog.security.exception.AccountExpiredException;
import com.blog.security.exception.AccountLoginByOtherException;
import com.blog.security.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;


@Component
public class JwtBackStageFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        // 获取URI并判断是否属于后台路径
        String requestURI = httpServletRequest.getRequestURI();

        if(!requestURI.startsWith("/back")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }


        String token = httpServletRequest.getHeader("token");


        Result result = null;

        if(token == null || "".equals(token)) {
            result = new Result();
            result.error(ResultCode.USER_NOT_LOGIN);
            JSONAuthentication.writeJSON(httpServletRequest, httpServletResponse, result);
            return;
        }

        Map<String, Object> info = null;

        try {
             info = JWTUtils.verify(token);
        } catch (JWTDecodeException e) {
            result = new Result();
            result.error(ResultCode.SYSTEM_UNDER_ATTRACK);
        } catch (AccountExpiredException e) {
            result = new Result();
            result.error(ResultCode.USER_ACCOUNT_EXPIRED);
        } catch (AccountLoginByOtherException e) {
            result = new Result();
            result.error(ResultCode.USER_ACCOUNT_USE_BY_OTHERS);
        }
        if(result != null) {
            JSONAuthentication.writeJSON(httpServletRequest, httpServletResponse, result);
            return;
        }

        // 认证成功，设置令牌，第二部分的密码换成角色Code
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(info.get("username"), info.get("rolecode"), (Collection<? extends GrantedAuthority>) info.get("authorities"));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 重置token时间
        Token.updateExpirationTime((String) info.get("username"));

        // 调用下一个过滤器
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
