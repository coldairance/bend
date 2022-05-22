package com.blog.controller;

import com.alibaba.fastjson.JSON;
import com.blog.pojo.po.Role;
import com.blog.security.utils.JSONAuthentication;
import com.blog.security.utils.Result;
import com.blog.service.api.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Api(tags = "后台接口", value = "权限：back:login")
@RestController
@PreAuthorize("hasAuthority('back:login')")
@RequestMapping("/back")
public class BackController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "插入新用户")
    @GetMapping("user/insert")
    @PreAuthorize("hasAuthority('back:user:operation')")
    public void insertUser(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam Integer roleID
    ) throws IOException {
        userService.insert(username, password, roleID);
        Result result = new Result();
        result.ok();
        JSONAuthentication.writeJSON(request, response, result);
    }

    @ApiOperation(value = "用户名查重")
    @GetMapping("user/name/checkdouble")
    @PreAuthorize("hasAuthority('back:user:operation')")
    public void checkUserName(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam String username
    ) throws IOException {

        Result result = new Result();
        result.ok();

        Boolean change = userService.checkUserName(username);
            if(!change) {
                // 没有这个用户名
                result.data("save", true);
            }else {
                result.data("save", false);
            }

        JSONAuthentication.writeJSON(request, response, result);
    }


    @ApiOperation(value = "获取全部角色")
    @GetMapping("roles/get")
    public void getRoles(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        List<Role> roles = userService.getRoles();
        Result result = new Result();
        result.ok().data("roles",roles);
        JSONAuthentication.writeJSON(request,response,result);
    }


    @ApiOperation(value = "模糊查询用户")
    @GetMapping("user/query/fuzzy")
    public void getUserFuzzy(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            @RequestParam("username") String username
    ) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer rolecode = (Integer) authentication.getCredentials();
        List<String> us =  userService.getUserByFuzzy(username, rolecode);
        List<String> users = new ArrayList<>();
        if(us.size() < 10) {
            users = us;
        }else {
            for (int i = 0; i < 10; i++) {
                users.add(us.get(i));
            }
        }
        Result result = new Result();
        result.ok().data("users", users);
        JSONAuthentication.writeJSON(httpServletRequest, httpServletResponse,result);
    }

    @ApiOperation(value = "根据用户名查询角色")
    @GetMapping("user/role")
    public void getRolesByName(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            @RequestParam("username") String username
    ) throws IOException {
        List<Role> rolesByUserName = userService.getRolesByUserName(username);
        List<String> roleNames = new ArrayList<>();
        rolesByUserName.forEach(role -> {
            roleNames.add(role.getRoleName());
        });
        Result result = new Result();
        result.ok().data("roles", roleNames);
        JSONAuthentication.writeJSON(httpServletRequest, httpServletResponse,result);
    }

    @ApiOperation(value = "根据用户名删除用户", notes = "权限：back:operation")
    @GetMapping("user/delete")
    @PreAuthorize("hasAuthority('back:user:operation')")
    public void deleteUser(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            @RequestParam("username") String username
    ) throws IOException {
        userService.deleteUserByName(username);
        Result result = new Result();
        result.ok();
        JSONAuthentication.writeJSON(httpServletRequest,httpServletResponse,result);
    }

    @ApiOperation(value = "根据用户名数组批量删除用户", notes = "权限：back:operation")
    @PostMapping("user/delete/many")
    @PreAuthorize("hasAuthority('back:user:operation')")
    public void deleteUsers(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            @RequestBody String usernames
    ) throws IOException {
        List<String> strings = JSON.parseArray(usernames, String.class);
        userService.deleteUsersByName(strings);
        Result result = new Result();
        result.ok();
        JSONAuthentication.writeJSON(httpServletRequest,httpServletResponse,result);
    }

    @ApiOperation(value = "获取用户总数")
    @GetMapping("user/total")
    public void getUserTotal(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer rolecode = (Integer) authentication.getCredentials();
        Integer total = userService.getTotal(rolecode);
        Result result = new Result();
        result.data("total", total);
        result.ok();
        JSONAuthentication.writeJSON(httpServletRequest,httpServletResponse,result);
    }

    @ApiOperation(value = "根据页码和大小获取一页的用户信息")
    @GetMapping("user/info")
    public void getUserInfo(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size
    ) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer rolecode = (Integer) authentication.getCredentials();

        List<String> usersByPage = userService.getUsernamesByPage(page, size, rolecode);

        Map<String, List<String>> rolesByUsernames = userService.getRolesByUsernames(usersByPage);


        ArrayList<Object> objects = new ArrayList<>();
        Result result = new Result();

        rolesByUsernames.forEach((k,v) -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", k);
            map.put("roles", v);
            objects.add(map);
        });
        result.data("users", objects);
        result.ok();
        JSONAuthentication.writeJSON(httpServletRequest, httpServletResponse, result);
    }

}
