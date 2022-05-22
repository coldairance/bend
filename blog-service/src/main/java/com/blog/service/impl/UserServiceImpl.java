package com.blog.service.impl;

import com.blog.mapper.UserMapper;
import com.blog.pojo.po.Role;
import com.blog.pojo.po.User;
import com.blog.pojo.vo.ArticalInfo;
import com.blog.pojo.vo.UserInfo;
import com.blog.security.utils.JWTUtils;
import com.blog.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;

// 设置"只读"提高速度
@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Value("${picture.real.info}")
    private String info_path;

    @Override
    @Transactional(readOnly = false)
    public void insert(String username, String pwd, Integer roleID) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(pwd);
        userMapper.insert(username,encode);
        Integer userID = userMapper.selectLastInsertId();
        userMapper.insertRole(userID, roleID);
    }

    @Override
    public List<String> getUsernamesByPage(Integer page, Integer size, Integer roleCode) {
        return userMapper.selectUsernamesByOffset((page-1) * size, size, roleCode);
    }

    @Override
    public List<Role> getRolesByUserName(String username) {
        return userMapper.selectRolesByUserName(username);
    }

    @Override
    public UserInfo getUserInfoByUserName(String username) {
        return userMapper.selectPersonInfoByUserName(username);
    }

    @Override
    public List<String> getAuthoritiesByUsername(String username) {
        return userMapper.selectAuthoritiesByUsername(username);
    }

    @Override
    public Map<String,List<String>> getRolesByUsernames(List<String> usernames) {
        List<Map<String,String>> roleInfos = userMapper.selectRolesByUsernames(usernames);
        // 存储用户及权限信息
        Map<String,List<String>> map = new LinkedHashMap<>();


        final String[] username = {""};
        roleInfos.forEach(roleInfo -> {
            if(username[0].equals(roleInfo.get("username"))) {
                map.get(username[0]).add(roleInfo.get("rolename"));
            }else {
                username[0] = roleInfo.get("username");
                List<String> authorities = new ArrayList<>();
                authorities.add(roleInfo.get("rolename"));
                map.put(roleInfo.get("username"), authorities);
            }
        });

        return map;
    }

    @Override
    public Integer getTotal(Integer roleCode) {
        return userMapper.selectSize(roleCode);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteUserByName(String username) {
        userMapper.deleteByUserName(username);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteUsersByName(List<String> strings) {
        userMapper.deleteUsersByUserNames(strings);
    }

    @Override
    public List<String> getUserByFuzzy(String username, Integer rolecode) {
        return userMapper.selectUserByFuzzy(username,rolecode);
    }

    @Override
    @Transactional(readOnly = false)
    public void setHeaderByUsername(String username, String url) {
        String old = userMapper.selectHeaderByUsername(username);
        if(old != null && !old.equals("")) {
            String path = info_path+old.substring(old.indexOf("picture/")+8);
            File file = new File(path);

            if(file.exists())
                file.delete();
        }
        userMapper.updateHeaderByUsername(username, url);
    }

    @Override
    @Transactional(readOnly = false)
    public void setBackgroundByUsername(String username, String url) {
        String old = userMapper.selectBackgroundByUsername(username);
        if(old != null && !old.equals("")) {
            String path = info_path+old.substring(old.indexOf("picture/")+8);
            File file = new File(path);

            if(file.exists())
                file.delete();
        }
        userMapper.updateBackgroundByUsername(username, url);
    }

    @Override
    @Transactional(readOnly = false)
    public String setUser(User user, String oldname) {
        Integer id = userMapper.selectIDByUserName(oldname);

        if(user.getUserPwd() != null) {
            // 加密
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setUserPwd(encoder.encode(user.getUserPwd()));
        }

        user.setUserId(id);
        userMapper.updateUser(user);
        HashMap<String, String> map = new HashMap<>();
        String name = user.getUserName();
        Date date = new Date();
        map.put("username", name);
        map.put("login-time", date.getTime()+"");

        // 获得token
       return JWTUtils.getToken(map);
    }

    @Override
    public Boolean checkUserName(String usrename) {
        return userMapper.checkUserName(usrename);
    }

    @Override
    @Transactional(readOnly = false)
    public Integer insertArticalByUsername(String username, String path, String title, String description, Boolean status, String type, Integer specialID) {
        userMapper.insertArticalByUsername(username, path, title, description, status, type, specialID);
        return userMapper.selectLastInsertId();
    }

    @Override
    public List<Role> getRoles() {
        return userMapper.selectRoles();
    }

}
