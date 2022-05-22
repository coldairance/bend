package com.blog.service.api;

import com.blog.pojo.po.Artical;
import com.blog.pojo.vo.ArticalInfo;
import com.blog.pojo.vo.MdInfo;
import com.blog.pojo.vo.UserInfo;

import java.util.List;

public interface ArticalService {

    void setArticalByArticalID(Artical artical);

    void insertPicturesByArticalID(Integer id, List<String> urls);


    List<String> getPicturesByArticalID(Integer id);


    void deletePicturesByArticalID(Integer id, List<String> urls);

    void deleteArticalByArticalID(Integer id);

    // @Cacheable(cacheNames = {"md-info"},unless = "#result==null", key="#id")
    MdInfo getMdByArticalID(Integer id);

    // @Cacheable(cacheNames = {"user-info"},unless = "#result==null", key="#id")
    UserInfo getUserInfoByArticalID(Integer id);

    List<ArticalInfo> getArticalsByLocal(String username, Integer page, Integer size);

    List<ArticalInfo> getArticalsByOther(String username, Integer page, Integer size);

    List<ArticalInfo> getArticalsByDate(Integer page, Integer size);

    // @Cacheable(cacheNames = {"artical-important"},unless = "#result==null")
    List<ArticalInfo> getArticalsForImportant();

}
