package com.bend.controller;

import cn.hutool.http.server.HttpServerRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bend.dao.ArticleMapper;
import com.bend.dao.BelongMapper;
import com.bend.dao.CommentMapper;
import com.bend.entity.Article;
import com.bend.entity.Belong;
import com.bend.entity.Comment;
import com.bend.utils.Result;
import com.bend.utils.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

@RestController
@RequestMapping("/get")
public class GetController {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private BelongMapper belongMapper;

    @Autowired
    private CommentMapper commentMapper;

    // md文件路径
    @Value("$path.md")
    private String md_path;

    @GetMapping("end")
    public Result getEnd(
            HttpServerRequest request,
            HttpServletResponse response,
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam Integer type,
            @RequestParam Integer belong
    )  {
        QueryWrapper<Article> wrapper = new QueryWrapper<Article>().orderByDesc("update_time");
        if(type!=0) {
            wrapper.eq("type",type);
        }
        if(type==1&&belong>0) {
            wrapper.eq("belong",belong);
        }
        Page<Article> current = articleMapper.selectPage(new Page<Article>(page, size), wrapper);
        Result ret = new Result().ok();
        ret.add("records", current.getRecords())
                .add("total", current.getTotal());

        return ret;
    }

    @GetMapping("front")
    public Result getFront(
            HttpServerRequest request,
            HttpServletResponse response,
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam Integer type,
            @RequestParam Integer belong
    )  {
        QueryWrapper<Article> wrapper = new QueryWrapper<Article>().orderByDesc("update_time").eq("state",1);
        if(type!=0) {
            wrapper.eq("type",type);
        }
        if(type==1&&belong>0) {
            wrapper.eq("belong",belong);
        }
        Page<Article> current = articleMapper.selectPage(new Page<Article>(page, size), wrapper);
        Result ret = new Result().ok();
        ret.add("records", current.getRecords())
                .add("total", current.getTotal());

        return ret;
    }

    @GetMapping("belong")
    public Result getBelong(

    ) {
        List<Belong> belongs = belongMapper.selectList(null);
        Result ret = new Result().ok();
        ret.add("belongs", belongs);

        return ret;
    }

    @GetMapping("md")
    public Result getMd(
            HttpServerRequest request,
            HttpServletResponse response,
            @RequestParam String aid
    ) {

        Article article = articleMapper.selectById(aid);
        Result result = new Result();
        File file = new File(article.getPath());
        if(!file.exists()) {
            result.error(ResultCode.FILE_NOT_FOUND);
            return result;
        }

        byte[] bytes = new byte[(int) file.length()];

        FileInputStream fileInputStream = null;
        String content = "";
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytes);

            fileInputStream.close();


            content = new String(bytes, "UTF-8");
        } catch (Exception e) {
            result.error(ResultCode.FILE_GET_FAILURE);
            return result;
        }

        result.ok()
                .add("md", content);

        return result;
    }

    @GetMapping("comment")
    public Result getComment(
            HttpServerRequest request,
            HttpServletResponse response,
            @RequestParam String aid
    ) {
        List<Comment> comments = commentMapper.selectList(new QueryWrapper<Comment>().eq("aid", aid));

        Result ok = new Result().ok();

        ok.add("comments",comments);

        return ok;
    }
}
