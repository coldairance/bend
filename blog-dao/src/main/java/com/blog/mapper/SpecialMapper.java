package com.blog.mapper;

import com.blog.pojo.vo.ArticalInfo;
import com.blog.pojo.vo.SpecialInfo;

import java.util.List;

public interface SpecialMapper {


    // 根据专题ID查找用户名
    String getUserName(Integer id);

    // 根据专题ID批量查找文章
    List<ArticalInfo> selectArticalsBySpecialID(Integer id);

    // 插入专题
    void insertSpecial(String username,String title);

    // 专题名查重
    Integer checkSpecialName(String username,String title);

    // 更新背景图片地址
    void updateBackground(Integer id, String url);

    // 更改专题名
    void updateSpecialTitle(Integer id,String title);

    // 删除专题
    void deleteSpecial(Integer id);

    // 获取背景图片地址
    String selectBackground(Integer id);

    // 批量获取专题
    List<SpecialInfo> selectSpecialsByUserName(String username);


    Integer selectLastInsertId();
}
