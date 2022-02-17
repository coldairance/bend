package com.bend.controller;

import cn.hutool.http.server.HttpServerRequest;
import com.bend.dao.ArticleMapper;
import com.bend.dao.CommentMapper;
import com.bend.entity.Article;
import com.bend.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

@RestController
@RequestMapping("/delete")
public class DeleteController {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CommentMapper commentMapper;

    @GetMapping("article")
    public Result deleteArticle(
            HttpServerRequest request,
            HttpServletResponse response,
            @RequestParam String aid
    ) {

        Article article = articleMapper.selectById(aid);
        String path = article.getPath();
        File file = new File(path);
        if(file.exists()){
            file.delete();
        }

        articleMapper.deleteById(aid);

        Result result = new Result().ok();
        return result;
    }

    @GetMapping("comment")
    public Result deleteComment(
            HttpServerRequest request,
            HttpServletResponse response,
            @RequestParam Integer cid
    ) {
        commentMapper.deleteById(cid);
        Result ok = new Result().ok();
        return ok;
    }
}
