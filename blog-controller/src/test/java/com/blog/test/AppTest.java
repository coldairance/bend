package com.blog.test;

import com.blog.mapper.ArticalMapper;
import com.blog.mapper.UserMapper;
import com.blog.pojo.po.User;
import com.blog.pojo.vo.ArticalInfo;
import com.blog.pojo.vo.UserInfo;
import com.blog.service.api.ArticalService;
import com.blog.service.api.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
//@SpringBootTest
public class AppTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticalMapper articalMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ArticalService articalService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void test() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("132574APPLE");
        System.out.println(encode);
    }


//    @Test
//    void test1() {
//        List<String> urls = new ArrayList<>();
//        urls.add("fasfsda.png");
//        urls.add("fsadfsda.png");
//        urls.add("fsafsadf.jpg");
//        userMapper.deletePicturesByArticalID(1, urls);
//    }



    @Test
    void testConnection() throws SQLException {
        Connection connection = dataSource.getConnection();

        log.info(connection.toString());
    }

    @Test
    void testMapper() {
//        for (int i = 0; i < 200; i++) {
//            User user = new User();
//            user.setUserName("用户"+(i+84));
//            userMapper.insert(user);
//            userMapper.insertRoleByUserName(user.getUserName(), "user");
//        }
        //List<String> strings = userMapper.selectUserByFuzzy("4");
        //System.out.println(strings);
    }

//    @Test
//    void testService() {
//        User user = userService.getUserById(1);
//    }

//    @Test
//    void testTX() {
//        User user = new User();
//
//        user.setUserName("小张");
//
//        userService.insert(user);
//
//    }

//    @Test
//    void testJwt() {
//        List<GrantedAuthority> verify = JWTUtils.verify("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpbi10aW1lIjoiMTYxODcyNjY4MTY5OSIsInVzZXJuYW1lIjoieGlhb21pbmcifQ.g7E1Y2tpbcFCDfQo-UFAhFw5qEhy_9I6-iiHO6pUgac");
//
//        System.out.println(verify);
//
//    }

   @Test
   void testRedis() {
        String token = "GDSFAGADFSG.GHSFGHSFH.GHDFSHSDFH";
        stringRedisTemplate.opsForValue().set(token, token);
   }


}
