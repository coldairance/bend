package com.blog.service.api;

import com.blog.pojo.po.Role;
import com.blog.pojo.po.User;
import com.blog.pojo.vo.UserInfo;

import java.util.List;
import java.util.Map;

public interface UserService {


    void insert(String username, String pwd, Integer roleID);

    List<String> getUsernamesByPage(Integer page, Integer size, Integer rolecode);

    List<Role> getRolesByUserName(String username);

    UserInfo getUserInfoByUserName(String username);

    List<String> getAuthoritiesByUsername(String username);

    Map<String,List<String>> getRolesByUsernames(List<String> usernames);

    Integer getTotal(Integer rolecode);

    void deleteUserByName(String username);

    void deleteUsersByName(List<String> strings);

    List<String> getUserByFuzzy(String username, Integer rolecode);

    void setHeaderByUsername(String username, String url);

    void setBackgroundByUsername(String username, String url);

    String setUser(User user, String oldname);

    Boolean checkUserName(String usrename);

    Integer insertArticalByUsername(String username, String path, String title, String description, Boolean status, String type, Integer specialID);

    List<Role> getRoles();
}
