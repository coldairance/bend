package com.blog.service.api;

import com.blog.pojo.vo.ArticalInfo;
import com.blog.pojo.vo.SpecialInfo;

import java.util.List;

public interface SpecialService {

    // 插入专题
    Integer setSpecial(String username,String title);

    // 改变背景
    void setBackground(Integer id, String url);

    // 改变标题
    void setTitle(Integer id, String title);

    // 删除专题
    void removeSpecial(Integer id);

    // 获取用户名
    String getUserNameBySpecialID(Integer id);

    // 批量获取专题
    List<SpecialInfo> getSpecialsByUserName(String username);

    // 根据专题ID批量获取文章信息
    List<ArticalInfo> getArticalsBySpecialID(Integer id);

    // 专题名查重
    Boolean checkSpecialTitleDouble(String username,String title);
}
