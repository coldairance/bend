package com.blog.mapper;

import com.blog.pojo.po.Role;
import com.blog.pojo.po.User;
import com.blog.pojo.vo.UserInfo;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    Integer selectIDByUserName(String username);

    List<Role> selectRolesByUserName(String username);

    List<String> selectAuthorityNameByRoleName(String rolename);

    void insertRoleByUserName(String username, String rolename);

    void insertAuthorityByRoleName(String rolename, String authorityname);

    void deleteByUserName(String username);

    void deleteUsersByUserNames(List<String> usernames);

    List<String> selectUserByFuzzy(String username,Integer rolecode);

    List<String> selectUsernamesByOffset(Integer offset, Integer size, Integer rolecode);


    Integer selectSize(Integer rolecode);

    // 根据用户名获取用户最高权限角色Code
    Integer selectRoleCodeByUsername(String username);

    // 根据用户名获取用户权限
    List<String> selectAuthoritiesByUsername(String username);

    // 根据用户名批量获取用户角色
    List<Map<String,String>> selectRolesByUsernames(List<String> usernames);

    // 根据用户名得到头像地址
    String selectHeaderByUsername(String username);

    // 根据用户名得到背景地址
    String selectBackgroundByUsername(String username);

    // 根据用户名更改头像地址
    void updateHeaderByUsername(String username, String url);

    // 根据用户名更改背景图片地址
    void updateBackgroundByUsername(String username, String url);

    // 根据用户名保存文章
    void insertArticalByUsername(String username, String path, String title, String description, Boolean status, String type, Integer SpecialID);

    // 根据用户名获取主页信息
    UserInfo selectPersonInfoByUserName(String username);

    // 获取自增ID
    Integer selectLastInsertId();

    // 获取全部角色
    List<Role> selectRoles();

    User selectUserByUserName(String username);

    // 更改用户
    void updateUser(User user);

    // 用户名查重
    Boolean checkUserName(String username);

    // 插入用户
    void insert(String username,String pwd);

    // 插入角色
    void insertRole(Integer userID, Integer roleID);

}
