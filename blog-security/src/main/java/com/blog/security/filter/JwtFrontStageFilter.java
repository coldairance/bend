package com.blog.security.filter;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.blog.security.exception.AccountExpiredException;
import com.blog.security.exception.AccountLoginByOtherException;
import com.blog.security.utils.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtFrontStageFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        // 获取URI并判断是否属于前台路径
        String requestURI = httpServletRequest.getRequestURI();

        if(!requestURI.startsWith("/front")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String token = httpServletRequest.getHeader("token");

        if(token == null) {
            // 设置无权限
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(null, null, null);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        Result result = null;
        String username = null;

        try {
            username = JWTUtils.check(token);
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
            // 通知用户
            JSONAuthentication.writeJSON(httpServletRequest, httpServletResponse, result);
            return;
        }


        List<GrantedAuthority> auth = new ArrayList<>();
        auth.add(new SimpleGrantedAuthority("login"));

        // 认证成功，设置令牌权限为 login
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, auth);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 重置token时间
        Token.updateExpirationTime(username);

        // 调用下一个过滤器
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
