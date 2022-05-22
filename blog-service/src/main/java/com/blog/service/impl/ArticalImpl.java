package com.blog.service.impl;

import com.blog.mapper.ArticalMapper;
import com.blog.pojo.po.Artical;
import com.blog.pojo.vo.ArticalInfo;
import com.blog.pojo.vo.MdInfo;
import com.blog.pojo.vo.UserInfo;
import com.blog.service.api.ArticalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Transactional
@Service
public class ArticalImpl implements ArticalService {

    @Autowired
    private ArticalMapper articalMapper;


    @Override
    public void setArticalByArticalID(Artical artical) {
        articalMapper.updateArticalByArticalID(artical);
    }

    @Override
    public void insertPicturesByArticalID(Integer id, List<String> urls) {
        articalMapper.insertPicturesByArticalID(id, urls);
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> getPicturesByArticalID(Integer id) {
        return articalMapper.selectPicturesByArticalID(id);
    }

    @Override
    public void deletePicturesByArticalID(Integer id, List<String> urls) {
        articalMapper.deletePicturesByArticalID(id,urls);
    }

    @Override
    public void deleteArticalByArticalID(Integer id) {
        articalMapper.deleteArticalByArticalID(id);
    }

    @Override
    public MdInfo getMdByArticalID(Integer id) {
        return articalMapper.selectMdByArticalID(id);
    }

    @Override
    public UserInfo getUserInfoByArticalID(Integer id) {
        return articalMapper.selectUserInfoByArticalID(id);
    }

    @Override
    public List<ArticalInfo> getArticalsByLocal(String username, Integer page, Integer size) {
        return articalMapper.selectArticalsByLocal(username, (page-1)*size, size);
    }

    @Override
    public List<ArticalInfo> getArticalsByOther(String username, Integer page, Integer size) {
        return articalMapper.selectArticalsByOther(username, (page-1)*size, size);
    }

    @Override
    public List<ArticalInfo> getArticalsByDate(Integer page, Integer size) {
        return articalMapper.selectArticalsByDate((page-1)*size, size);
    }

    @Override
    public List<ArticalInfo> getArticalsForImportant() {
        return articalMapper.selectArticalsForImportant();
    }
}
