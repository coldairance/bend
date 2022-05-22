package com.blog.security.auth;

import com.blog.mapper.UserMapper;
import com.blog.pojo.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(value = "userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userMapper.selectUserByUserName(username);

        if(user == null) {
            throw new InternalAuthenticationServiceException("用户不存在");
        }

        // 查询用户的权限集合
        List<GrantedAuthority> authorities = new ArrayList<>();


        List<String> auths = userMapper.selectAuthoritiesByUsername(username);

        // 保存权限信息
        auths.forEach(auth -> {
            authorities.add(new SimpleGrantedAuthority(auth));
        });

        // 传入权限
        return new org.springframework.security.core.userdetails.User(username, user.getUserPwd(), authorities);
    }
}
