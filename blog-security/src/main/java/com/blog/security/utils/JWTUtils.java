package com.blog.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.blog.mapper.ArticalMapper;
import com.blog.mapper.UserMapper;
import com.blog.security.exception.AccountExpiredException;
import com.blog.security.exception.AccountLoginByOtherException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Component
public class JWTUtils {


    private static UserMapper userMapper;

    private static ArticalMapper articalMapper;

    @Autowired
    public void setArticalMapper(ArticalMapper articalMapper) {
        JWTUtils.articalMapper = articalMapper;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        JWTUtils.userMapper = userMapper;
    }

    private static String SING;

    @Value("${jwt.key}")
    public void setSING(String SING) {
        JWTUtils.SING = SING;
    }


    /**
     * 生成token  header.payload.signature
     */
    public static String getToken(Map<String, String> map) {


        // 创建jwt builder
        JWTCreator.Builder builder = JWT.create();

        // payload
        map.forEach((k,v) -> {
            builder.withClaim(k, v);
        });

        String token = builder.sign(Algorithm.HMAC256(SING));

        return token;
    }


    /**
     * 验证 token 合法性 并返回权限信息
     */
    public static Map<String, Object> verify(String token) {

        // 创建验证对象
        JWTVerifier build = JWT.require(Algorithm.HMAC256(SING)).build();

        // 解码信息
        DecodedJWT verify = build.verify(token);

        // 获取用户名
        String username = verify.getClaim("username").asString();

        String old = Token.get(username);

        if(old == null) {
            // 账号过期
            throw new AccountExpiredException("账号过期");
        }

        if(!old.equals(token)) {
            // 账号在其他地方登录
            throw new AccountLoginByOtherException("账号被他人登录");
        }

        HashMap<String, Object> info = new HashMap<>();



        // 查询用户的权限集合
        List<GrantedAuthority> authorities = new ArrayList<>();


        List<String> auths = userMapper.selectAuthoritiesByUsername(username);

        System.out.println(auths);

        // 保存权限信息
        auths.forEach(auth -> {
            authorities.add(new SimpleGrantedAuthority(auth));
        });

        // 查询用户的最大权限角色code
        Integer code = userMapper.selectRoleCodeByUsername(username);


        info.put("username", username);
        info.put("authorities", authorities);
        info.put("rolecode", code);

        // 验证通过，返回权限信息
        return info;
    }


    /**
     * 验证 token 是否正确
     */
    public static String check(String token) {
        // 创建验证对象
        JWTVerifier build = JWT.require(Algorithm.HMAC256(SING)).build();

        // 解码信息
        DecodedJWT verify = build.verify(token);

        // 获取用户名
        String username = verify.getClaim("username").asString();

        String old = Token.get(username);

        if(old == null) {
            // 账号过期
            throw new AccountExpiredException("账号过期");
        }

        if(!old.equals(token)) {
            // 账号在其他地方登录
            throw new AccountLoginByOtherException("账号被他人登录");
        }

        return username;
    }


    /**
     * 验证是否为本人
     */
    public static void checkLocal(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getPrincipal().equals(username)) {
            throw new AccessDeniedException("无权限");
        }
    }

    /**
     * 验证Artical是否存在
     */
    public static String checkArtical(HttpServletRequest request, HttpServletResponse response, Integer id) throws IOException {
        System.out.println(id);
        String s = articalMapper.selectUserNameByArticalID(id);

        Result result = new Result();

        if(s == null || s.equals("")) {
            result.error(ResultCode.FILE_NOT_FOUND);
            JSONAuthentication.writeJSON(request, response, result);
            return null;
        }
        return s;
    }
}
