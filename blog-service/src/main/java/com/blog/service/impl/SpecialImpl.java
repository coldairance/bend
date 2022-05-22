package com.blog.service.impl;

import com.blog.mapper.ArticalMapper;
import com.blog.mapper.SpecialMapper;
import com.blog.pojo.vo.ArticalInfo;
import com.blog.pojo.vo.SpecialInfo;
import com.blog.service.api.SpecialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

@Transactional
@Service
public class SpecialImpl implements SpecialService {

    // 普通图片路径
    @Value("${picture.real.common}")
    private String common_path;

    @Autowired
    SpecialMapper specialMapper;

    @Autowired
    ArticalMapper articalMapper;

    @Override
    public Integer setSpecial(String username,String title) {
        specialMapper.insertSpecial(username, title);
        return specialMapper.selectLastInsertId();
    }

    @Override
    public void setBackground(Integer id, String url) {
        String old = specialMapper.selectBackground(id);
        if(old != null && !old.equals("")) {
            String path = common_path+old.substring(old.indexOf("picture/")+8);
            File file = new File(path);

            if(file.exists())
                file.delete();
        }
        specialMapper.updateBackground(id, url);
    }

    @Override
    public void setTitle(Integer id, String title) {
        specialMapper.updateSpecialTitle(id, title);
    }

    @Override
    public void removeSpecial(Integer id) {
        String path = specialMapper.selectBackground(id);
        if(path != null && !path.equals("")) {
            String url = common_path+path.substring(path.indexOf("picture/")+8);
            File file = new File(path);

            if(file.exists())
                file.delete();
        }
        specialMapper.deleteSpecial(id);
    }

    @Override
    @Transactional(readOnly = true)
    public String getUserNameBySpecialID(Integer id) {
        return specialMapper.getUserName(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpecialInfo> getSpecialsByUserName(String usrname) {
        return specialMapper.selectSpecialsByUserName(usrname);
    }

    @Override
    public List<ArticalInfo> getArticalsBySpecialID(Integer id) {
        return specialMapper.selectArticalsBySpecialID(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean checkSpecialTitleDouble(String username, String title) {
        return specialMapper.checkSpecialName(username,title) > 0;
    }
}
