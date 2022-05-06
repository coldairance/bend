package com.bend.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.server.HttpServerRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bend.dao.ArticleMapper;
import com.bend.dao.CommentMapper;
import com.bend.entity.Article;
import com.bend.entity.Comment;
import com.bend.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/add")
public class AddController {


    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CommentMapper commentMapper;

    // md文件路径
    @Value("${path.md}")
    private String md_path;


    @PostMapping("article")
    public Result addArticle(
            HttpServerRequest request,
            HttpServletResponse response,
            @RequestBody String data
    ) {
        Article article = new Article();
        article.setAid(IdUtil.randomUUID());
        JSONObject jsonObject = JSONUtil.parseObj(data);
        String first = jsonObject.getStr("first");
        String second = jsonObject.getStr("second");
        Integer belong = jsonObject.getInt("belong");
        Integer type = jsonObject.getInt("type");
        String path = md_path+article.getAid()+".md";
        article.setFirst(first);
        article.setSecond(second);
        article.setBelong(belong);
        article.setType(type);
        article.setPath(path);

        articleMapper.insert(article);

        Result result = new Result().ok();
        return result;
    }

    @PostMapping("comment")
    public Result addComment(
            HttpServerRequest request,
            HttpServletResponse response,
            @RequestBody String data
    ) {
        JSONObject jsonObject = JSONUtil.parseObj(data);
        Comment comment = JSONUtil.toBean(data, Comment.class);
        comment.setTime(LocalDateTime.now());

        commentMapper.insert(comment);

        Result ok = new Result().ok();

        return ok;
    }
}
