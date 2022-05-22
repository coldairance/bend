package com.blog.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

// 开启Swagger
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket docket() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2);

        // API帮助文档的描述信息
        ApiInfo apiInfo =
                new ApiInfoBuilder()
                    .contact(new Contact("博客 - coldairance", "http://www.coldairance.com", "2645868345@qq.com"))
                    .title("接口文档")
                    .version("1.0")
                    .build();
        docket.apiInfo(apiInfo);

        docket
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.blog.controller"))
                .build();

        return docket;
    }
}
