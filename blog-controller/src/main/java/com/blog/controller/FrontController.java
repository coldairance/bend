package com.blog.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blog.pojo.po.Artical;
import com.blog.pojo.po.User;
import com.blog.pojo.vo.ArticalInfo;
import com.blog.pojo.vo.MdInfo;
import com.blog.pojo.vo.SpecialInfo;
import com.blog.pojo.vo.UserInfo;
import com.blog.security.utils.*;
import com.blog.service.api.ArticalService;
import com.blog.service.api.SpecialService;
import com.blog.service.api.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Api(tags = "前台接口")
@RestController
@RequestMapping("/front")
public class FrontController {


    // 域名
    @Value("${domain.name}")
    private String domain;

    // 图片访问路径
    @Value("${picture.mask}")
    private String maskPath_pic;

    // 图片真实路径
    // 用户信息图片路径
    @Value("${picture.real.info}")
    private String info_path;

    // 普通图片路径
    @Value("${picture.real.common}")
    private String common_path;


    // md文件路径
    @Value("${md.real}")
    private String md_path;


    @Autowired
    private UserService userService;

    @Autowired
    private ArticalService articalService;

    @Autowired
    private SpecialService specialService;

    @ApiOperation(value = "验证是否登录")
    @GetMapping("verify")
    public void verify(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getAuthorities().size() == 0) {
            Result result = new Result();
            result.error(ResultCode.USER_NOT_LOGIN);
            JSONAuthentication.writeJSON(request, response, result);
            return;
        }

        Result result = new Result();
        result.ok();
        JSONAuthentication.writeJSON(request, response, result);
    }

    @ApiOperation(value = "上传md文件")
    @PostMapping("upload/md")
    private void uploadMd(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody String md
    ) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getAuthorities().size() == 0) {
            throw new AccessDeniedException("");
        }

        String username = (String)authentication.getPrincipal();

        JSONObject jsonObject = JSON.parseObject(md);
        String title = jsonObject.getString("title");
        String description = jsonObject.getString("description");
        String content = jsonObject.getString("content");
        Boolean status = jsonObject.getBoolean("status");
        String type = jsonObject.getString("type");
        Integer specialId = jsonObject.getInteger("specialID");



        String name = UUID.randomUUID().toString()+".md";
        File file = new File(md_path + name);
        Result result = new Result();

        if(!file.exists()) {
            try {
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                // 将字符串转成字节
                byte[] bytes = content.getBytes();
                // 写入文件
                fileOutputStream.write(bytes);
                fileOutputStream.flush();
                // 关闭
                fileOutputStream.close();
                String path = md_path + name;


                Integer id = userService.insertArticalByUsername(username, path, title, description, status, type,specialId);

                result.ok().data("id",id);
                JSONAuthentication.writeJSON(request, response, result);
            } catch (IOException e) {
                result.error(ResultCode.UPLOAD_FAILURE);
                JSONAuthentication.writeJSON(request, response, result);
            }
        }
    }

    @ApiOperation(value = "上传图片")
    @PostMapping("upload/picture")
    private void uploadPicture(
            MultipartHttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("id") Integer id
    ) throws IOException {

        Result result = new Result();

        UserInfo userInfoByArticalID = articalService.getUserInfoByArticalID(id);

        String username = userInfoByArticalID.getUsername();

        if(username == null) {
            result.error(ResultCode.FILE_NOT_FOUND);
            JSONAuthentication.writeJSON(request, response, result);
            return;
        }

        JWTUtils.checkLocal(username);

        MultiValueMap<String, MultipartFile> multiFileMap = request.getMultiFileMap();
        List<MultipartFile> files = multiFileMap.get("files");
        File folder = new File(common_path);
        List<String> urls = new ArrayList<>();

        files.forEach(file -> {
            String oldName = file.getOriginalFilename();
            String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
            try {
                file.transferTo(new File(folder, newName));
            } catch (IOException e) {
                result.error(ResultCode.UPLOAD_FAILURE);
                try {
                    JSONAuthentication.writeJSON(request, response, result);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            String url = request.getScheme() + "://" + domain + "/" + maskPath_pic + newName;
            urls.add(url);
        });

        articalService.insertPicturesByArticalID(id, urls);

        result.ok().data("urls", urls);
        JSONAuthentication.writeJSON(request, response, result);
    }

    @ApiOperation(value = "保存用户信息图片")
    @PostMapping("upload/picture/cut/user")
    private void cutPictureForUser(
            MultipartFile file,
            @RequestParam Integer x,
            @RequestParam Integer y,
            @RequestParam Integer width,
            @RequestParam Integer height,
            @RequestParam String type,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getAuthorities().size() == 0) {
            throw new AccessDeniedException("");
        }

        String username = (String)authentication.getPrincipal();



        File folder = new File(info_path);
        String oldName = file.getOriginalFilename();
        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
        try {
            File pic = new File(folder, newName);
            file.transferTo(pic);
            ImageUtil.cut(pic, x, y, width, height);
        } catch (IOException e) {
            Result result = new Result();
            result.error(ResultCode.UPLOAD_FAILURE);
            JSONAuthentication.writeJSON(request, response, result);
        }
        String url = request.getScheme() + "://" + domain + "/" + maskPath_pic + newName;




        if("header".equals(type)) {
            userService.setHeaderByUsername(username, url);
        }else {
            userService.setBackgroundByUsername(username, url);
        }

        Result result = new Result();
        result.ok().data("url", url);
        JSONAuthentication.writeJSON(request, response, result);
    }

    @ApiOperation(value = "保存专题")
    @GetMapping("save/special")
    private void setSpecial(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam String title
    ) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getAuthorities().size() == 0) {
            throw new AccessDeniedException("");
        }

        String username = (String) authentication.getPrincipal();

        Integer id = specialService.setSpecial(username, title);

        Result result = new Result();
        result.ok().data("id", id);
        JSONAuthentication.writeJSON(request,response,result);
    }

    @ApiOperation(value = "保存专题背景图片")
    @PostMapping("upload/picture/cut/special")
    private void cutPictureForSpecial(
            MultipartFile file,
            @RequestParam Integer x,
            @RequestParam Integer y,
            @RequestParam Integer width,
            @RequestParam Integer height,
            @RequestParam Integer id,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        JWTUtils.checkLocal(username);

        File folder = new File(common_path);
        String oldName = file.getOriginalFilename();
        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
        try {
            File pic = new File(folder, newName);
            file.transferTo(pic);
            ImageUtil.cut(pic, x, y, width, height);
        } catch (IOException e) {
            Result result = new Result();
            result.error(ResultCode.UPLOAD_FAILURE);
            JSONAuthentication.writeJSON(request, response, result);
        }
        String url = request.getScheme() + "://" + domain+ "/" + maskPath_pic + newName;


        specialService.setBackground(id, url);


        Result result = new Result();
        result.ok().data("url", url);
        JSONAuthentication.writeJSON(request, response, result);
    }

    @ApiOperation(value = "批量获取专题")
    @GetMapping("get/special/batch")
    private void getSpecialsByUserName(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam String username
    ) throws IOException {
        List<SpecialInfo> specialsByUserName = specialService.getSpecialsByUserName(username);

        Result result = new Result();
        result.ok().data("specials", specialsByUserName);
        JSONAuthentication.writeJSON(request, response, result);
    }

    @ApiOperation(value = "专题名查重")
    @GetMapping("special/title/checkdouble")
    private void checkSpecialTitle(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam String title
    ) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getAuthorities().size() == 0) {
            throw new AccessDeniedException("");
        }
        String username = (String) authentication.getPrincipal();

        Result result = new Result();
        result.ok();
        Boolean exists = specialService.checkSpecialTitleDouble(username, title);
        if(exists) {
            result.data("change",false);
        }else {
            result.data("change",true);
        }

        JSONAuthentication.writeJSON(request, response, result);
    }

    @ApiOperation(value = "更改专题名")
    @GetMapping("special/update/title")
    private void updateSpecialTitle(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam Integer id,
            @RequestParam String title
    ) throws IOException {
        String username = specialService.getUserNameBySpecialID(id);
        JWTUtils.checkLocal(username);

        specialService.setTitle(id, title);

        Result result = new Result();
        result.ok();
        JSONAuthentication.writeJSON(request, response, result);
    }

    @ApiOperation(value = "删除专题")
    @GetMapping("special/delete")
    private void deleteSpecial(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam Integer id
    ) {
        String username = specialService.getUserNameBySpecialID(id);
        JWTUtils.checkLocal(username);
        specialService.removeSpecial(id);
    }

    @ApiOperation(value = "批量删除图片")
    @PostMapping("delete/pictures")
    private void deletePictures(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody String urls,
            @RequestParam Integer id
    ) throws IOException {

        Result result = new Result();

        UserInfo userInfoByArticalID = articalService.getUserInfoByArticalID(id);

        String username = userInfoByArticalID.getUsername();

        if(username == null) {
            result.error(ResultCode.FILE_NOT_FOUND);
            JSONAuthentication.writeJSON(request, response, result);
            return;
        }

        JWTUtils.checkLocal(username);

        List<String> pic_urls = JSON.parseArray(urls, String.class);

        pic_urls.forEach(url -> {

            File file = new File(common_path+url.substring(url.indexOf("picture/")+8));
            if (file.exists()) {
                file.delete();
            }
        });

        articalService.deletePicturesByArticalID(id,pic_urls);
        result.ok();
        JSONAuthentication.writeJSON(request, response, result);
    }

    @ApiOperation(value = "用户名查重")
    @GetMapping("user/name/checkdouble")
    public void checkUserName(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam String username
    ) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getAuthorities().size() == 0) {
            throw new AccessDeniedException("");
        }
        String oldname = (String) authentication.getPrincipal();
        boolean change;
        Result result = new Result();
        result.ok();

        if(oldname.equals(username)) {
            change = true;
            result.data("change", true);
        }else {
            change = userService.checkUserName(username);
            if(!change) {
                // 没有这个用户名，可以修改
                result.data("change", true);
            }else {
                result.data("change", false);
            }
        }

        JSONAuthentication.writeJSON(request, response, result);
    }

    @ApiOperation(value = "更新用户信息")
    @PostMapping("user/update/front")
    public void updateUser(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody String u
    ) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String oldname = (String) authentication.getPrincipal();
        JSONObject jsonObject = JSON.parseObject(u);
        String username = jsonObject.getString("username");

        if(authentication.getAuthorities().size() == 0 ) {

            throw new AccessDeniedException("");
        }

        User user = new User();
        user.setUserName(username);
        user.setUserPwd(jsonObject.getString("password"));
        user.setUserEmail(jsonObject.getString("email"));
        user.setUserPhone(jsonObject.getString("phone"));


        String token = userService.setUser(user, oldname);

        Token.set(username, token);

        Result result = new Result();
        result.ok().data("token", token);

        JSONAuthentication.writeJSON(request, response, result);
    }

    @ApiOperation(value = "获取用户信息")
    @GetMapping("user/info")
    public void getUserInfo(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam String username
    ) throws IOException {

        Result result = new Result();

        UserInfo userInfoByUserName = userService.getUserInfoByUserName(username);

        String info = JSON.toJSONString(userInfoByUserName);

        result.ok().data("info", info);

        JSONAuthentication.writeJSON(request, response, result);
    }

    @ApiOperation(value = "获取文章信息")
    @GetMapping("artical/info")
    public void getArticalInfo(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestParam Integer id
    ) throws IOException {

        Result result = new Result();

        String username = JWTUtils.checkArtical(request, response, id);

        JWTUtils.checkLocal(username);

        MdInfo mdByArticalID = articalService.getMdByArticalID(id);

        File file = new File(mdByArticalID.getPath());

        byte[] bytes = new byte[(int) file.length()];

        FileInputStream fileInputStream = new FileInputStream(file);

        fileInputStream.read(bytes);

        fileInputStream.close();


        String content = new String(bytes,"UTF-8");

        List<String> urls = articalService.getPicturesByArticalID(id);


        result.ok().data("md",content)
                .data("pictures",urls)
                .data("artical",mdByArticalID);

        JSONAuthentication.writeJSON(request, response, result);

    }

    @ApiOperation(value = "删除文章")
    @GetMapping("artical/delete")
    public void deleteArtical(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam Integer id
    ) throws IOException {

        Result result = new Result();

        String username = JWTUtils.checkArtical(request, response, id);

        JWTUtils.checkLocal(username);


        articalService.deleteArticalByArticalID(id);

        result.ok();

        JSONAuthentication.writeJSON(request, response, result);
    }

    @ApiOperation(value = "获取文章内容")
    @GetMapping("artical/content")
    public void getArtical(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam Integer id
    ) throws IOException {
        MdInfo mdByArticalID = articalService.getMdByArticalID(id);

        Result result = new Result();

        if(mdByArticalID.getStatus() == false) {
            result.error(ResultCode.ARTICAL_NOT_PUBLISH);
            JSONAuthentication.writeJSON(request, response, result);
            return;
        }



        File file = new File(mdByArticalID.getPath());

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
            JSONAuthentication.writeJSON(request, response, result);
        }

        UserInfo userInfoByArticalID = articalService.getUserInfoByArticalID(id);

        result.ok()
                .data("md", content)
                .data("username", userInfoByArticalID.getUsername())
                .data("header", userInfoByArticalID.getBackground());

        JSONAuthentication.writeJSON(request, response, result);
    }

    @ApiOperation(value = "更改文章信息")
    @PostMapping("artical/update")
    public void setArticalByArticalID(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody String artical
    ) throws IOException {

        JSONObject jsonObject = JSON.parseObject(artical);
        Integer id = jsonObject.getInteger("id");


        String username = JWTUtils.checkArtical(request, response, id);

        JWTUtils.checkLocal(username);

        String title = jsonObject.getString("title");
        String description = jsonObject.getString("description");
        Boolean status = jsonObject.getBoolean("status");
        String content = jsonObject.getString("content");
        String type = jsonObject.getString("type");
        Integer specialId = jsonObject.getInteger("specialID");

        MdInfo mdByArticalID = articalService.getMdByArticalID(id);

        String path = mdByArticalID.getPath();

        File file = new File(path);

        FileOutputStream fileOutputStream = null;

        Result result = new Result();

        try {
            fileOutputStream = new FileOutputStream(file);
            // 将字符串转成字节
            byte[] bytes = content.getBytes();
            // 写入文件
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            // 关闭
            fileOutputStream.close();
        } catch (Exception e) {
            result.error(ResultCode.UPDATE_FILE_FAILURE);
            JSONAuthentication.writeJSON(request, response, result);
        }


        Artical art = new Artical();
        art.setArticalID(id);
        art.setTitle(title);
        art.setDescription(description);
        art.setStatus(status);
        art.setType(type);
        art.setSpecialID(specialId);

        articalService.setArticalByArticalID(art);

        result.ok();
        JSONAuthentication.writeJSON(request, response, result);
    }

    @ApiOperation(value = "根据专题ID批量获取文章信息")
    @GetMapping("artical/get/batch/by/special")
    public void getArticalsBySpecialID(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam Integer id
    ) throws IOException {
        List<ArticalInfo> articalsBySpecialID = specialService.getArticalsBySpecialID(id);
        Result result = new Result();
        result.ok().data("articals", articalsBySpecialID);
        JSONAuthentication.writeJSON(request, response, result);
    }

    @ApiOperation(value = "本人批量获取文章信息")
    @GetMapping("artical/get/batch/local")
    public void getArticalsByLocal(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam Integer page,
            @RequestParam Integer size
    ) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getAuthorities().size() == 0) {
            throw new AccessDeniedException("");
        }

        String username = (String) authentication.getPrincipal();

        List<ArticalInfo> articalsByLocal = articalService.getArticalsByLocal(username, page, size);

        Result result = new Result();
        result.ok().data("articals",articalsByLocal);
        JSONAuthentication.writeJSON(request, response, result);
    }

    @ApiOperation(value = "他人批量获取文章信息")
    @GetMapping("artical/get/batch/other")
    public void getArticalsByOther(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam String username,
            @RequestParam Integer page,
            @RequestParam Integer size
    ) throws IOException {

        List<ArticalInfo> articalsByLocal = articalService.getArticalsByOther(username, page, size);

        Result result = new Result();
        result.ok().data("articals",articalsByLocal);
        JSONAuthentication.writeJSON(request, response, result);
    }

    @ApiOperation(value = "获取置顶文章信息")
    @GetMapping("artical/get/important")
    public void getArticalsForImportant(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        List<ArticalInfo> articalsByDate = articalService.getArticalsForImportant();

        Result result = new Result();
        result.ok().data("articals",articalsByDate);
        JSONAuthentication.writeJSON(request, response, result);
    }

    @ApiOperation(value = "根据更新时间批量获取文章信息")
    @GetMapping("artical/get/batch/date")
    public void getArticalsByDate(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam Integer page,
            @RequestParam Integer size
    ) throws IOException {

        List<ArticalInfo> articalsByDate = articalService.getArticalsByDate(page, size);


        Result result = new Result();
        result.ok().data("articals",articalsByDate);
        JSONAuthentication.writeJSON(request, response, result);
    }

}
