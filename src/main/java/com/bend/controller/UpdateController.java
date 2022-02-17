package com.bend.controller;

import cn.hutool.http.server.HttpServerRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bend.dao.ArticleMapper;
import com.bend.dao.CommentMapper;
import com.bend.entity.Article;
import com.bend.entity.Comment;
import com.bend.utils.Result;
import com.bend.utils.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/update")
public class UpdateController {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CommentMapper commentMapper;

    // md文件路径
    @Value("${path.md}")
    private String md_path;

    @PostMapping("md")
    public Result saveMD(
            HttpServerRequest request,
            HttpServletResponse response,
            @RequestBody String data
    ) {
        JSONObject jsonObject = JSONUtil.parseObj(data);
        String aid = jsonObject.getStr("aid");
        String raw = jsonObject.getStr("content");
        String path = md_path+aid+".md";

        File file = new File(path);
        Result result = new Result();

        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bytes = raw.getBytes(StandardCharsets.UTF_8);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        }catch (Exception e) {
            result.error(ResultCode.UPLOAD_FAILURE);
            return result;
        }

        Article article = articleMapper.selectById(aid);
        article.setUpdateTime(LocalDateTime.now());
        article.setPath(path);
        articleMapper.updateById(article);


        result.ok();
        return result;
    }

    @PostMapping("article")
    public Result saveArticle(
            HttpServerRequest request,
            HttpServletResponse response,
            @RequestBody String data
    ){
        JSONObject jsonObject = JSONUtil.parseObj(data);
        Article article = jsonObject.toBean(Article.class);
        article.setUpdateTime(LocalDateTime.now());
        articleMapper.updateById(article);

        Result ok = new Result().ok();

        return ok;
    }

    @PostMapping("comment")
    public Result saveComment(
            HttpServerRequest request,
            HttpServletResponse response,
            @RequestBody String data
    ){
        JSONObject jsonObject = JSONUtil.parseObj(data);
        Integer cid = jsonObject.getInt("cid");
        String answer = jsonObject.getStr("answer");

        Comment comment = commentMapper.selectById(cid);

        comment.setAnswer(answer);

        commentMapper.updateById(comment);

        Result ok = new Result().ok();

        return ok;
    }
}
