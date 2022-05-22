package com.blog.config;


import com.blog.security.auth.*;
import com.blog.security.filter.JwtBackStageFilter;
import com.blog.security.filter.JwtFrontStageFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
// 开启权限认证
@EnableWebSecurity
// 开启方法级验证
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // 配置密码加密
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 登录成功的处理器
     */
    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    /**
     * 登录失败的处理器
     */
    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    /**
     * 权限不足处理器
     */
    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;


    /**
     * 注入过滤器
     */
    @Autowired
    private JwtBackStageFilter jwtBackStageFilter;

    @Autowired
    private JwtFrontStageFilter jwtFrontStageFilter;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 使用自定义user策略
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 拦截所有请求
        http.authorizeRequests()
                // 放行login以及Swagger等资源路径
                .antMatchers("/picture/**","/login","/v2/api-docs","/swagger-resources","/swagger-resources/**","/webjars/**","/swagger-ui.html","/static").permitAll()
                // 所有请求都需要认证
                .anyRequest()
                .authenticated()
                // 设置登录
                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                // 使用处理器
                // 成功
                .successHandler(myAuthenticationSuccessHandler)
                // 失败
                .failureHandler(myAuthenticationFailureHandler)
                .and()
                // 异常处理器
                .exceptionHandling()
                // 权限不足异常处理器
                .accessDeniedHandler(myAccessDeniedHandler)
                .and()
                .sessionManagement()
                // 不使用session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // 添加自定义token过滤器
                .and()
                .addFilterAfter(jwtBackStageFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFrontStageFilter, JwtBackStageFilter.class)
                ;


        // 关闭跨域攻击
        http.csrf().disable();
    }
}
