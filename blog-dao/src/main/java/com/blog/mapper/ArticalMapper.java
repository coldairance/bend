package com.blog.mapper;

import com.blog.pojo.po.Artical;
import com.blog.pojo.vo.ArticalInfo;
import com.blog.pojo.vo.MdInfo;
import com.blog.pojo.vo.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticalMapper {

    // 根据文章ID更改文章
    void updateArticalByArticalID(Artical artical);

    // 根据文章ID删除文章
    void deleteArticalByArticalID(Integer id);

    // 根据文章id批量插入图片
    void insertPicturesByArticalID(@Param("id") Integer id, @Param("urls") List<String> urls);

    // 根据文章id批量查找图片
    List<String> selectPicturesByArticalID(Integer id);

    // 根据文章id批量删除图片
    void deletePicturesByArticalID(@Param("id") Integer id,@Param("urls") List<String> urls);

    // 根据文章id查找文章
    MdInfo selectMdByArticalID(Integer id);

    // 根据文章ID查找用户信息
    UserInfo selectUserInfoByArticalID(Integer id);

    // 根据文章ID查找用户名
    String selectUserNameByArticalID(Integer id);

    // 根据用户名批量获取文章数（本人）
    List<ArticalInfo> selectArticalsByLocal(String username, Integer offset, Integer size);

    // 根据用户名批量获取文章数（他人）
    List<ArticalInfo> selectArticalsByOther(String username, Integer offset, Integer size);

    // 根据更改日期获取文章
    List<ArticalInfo> selectArticalsByDate(Integer offset, Integer size);

    // 获取置顶文章
    List<ArticalInfo> selectArticalsForImportant();

}
