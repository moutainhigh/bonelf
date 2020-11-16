package com.bonelf.common.config.swagger;

import com.bonelf.common.constant.AuthConstant;
import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author bonelf
 */
@Slf4j
@Configuration
@EnableSwaggerBootstrapUI
@EnableOpenApi
@ConditionalOnProperty(name = "swagger.enable", havingValue = "true")
public class Swagger2Config implements WebMvcConfigurer {
    @Autowired
    private SwaggerCloudPathProvider pathProvider;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //SwaggerBootstrapUI
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        //SwaggerUI 3.0
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .resourceChain(false);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //SwaggerUI 3.0 使用redirect 网关访问地址栏会跳转到本服务端口地址，暴露端口地址
        registry.addViewController("/swagger-ui")
                .setViewName("redirect:/swagger-ui/index.html");
        //SwaggerUI 3.0  使用forward这里要在末尾加个"/"要不然网关访问会引起404（静态文件路径识别错误），同时访问时也要带"/"
        //registry.addViewController("/swagger-ui/")
        //        .setViewName("forward:/swagger-ui/index.html");
    }

    /**
     * swagger2的配置文件，这里可以配置swagger2的一些基本的内容，比如扫描的包等等
     *
     * @return Docket
     */
    @Bean
    @DependsOn("swaggerCloudPathProvider")
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                // 修改添加服务名称 如果想不启动网关调试并使用swagger调试要注释这个
                .pathProvider(pathProvider)
                .select()
                //此包路径下的类，才生成接口文档
                //.apis(RequestHandlerSelectors.basePackage("com.bonelf.testservice.controller"))
                //加了ApiOperation注解的类，才生成接口文档
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Collections.singletonList(securityScheme()))
                .securityContexts(securityContexts());
                //.globalOperationParameters(setHeaderToken());
    }

    /**
     * api文档的详细信息函数,注意这里的注解引用的是哪个
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // //大标题
                .title("Bonelf后台服务API接口文档")
                // 版本号
                .version("1.0")
//				.termsOfServiceUrl("NO terms of service")
                // 描述
                .description("后台API接口")
                // 作者
                .contact(new Contact("Bonelf","http://www.bonelf.com","ccykirito@163.com"))
                .license("The Apache License, Version 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .build();
    }

    /***
     * oauth2配置
     * 需要增加swagger授权回调地址
     * http://localhost:8888/webjars/springfox-swagger-ui/o2c.html
     * @return
     */
    @Bean
	SecurityScheme securityScheme() {
        return new ApiKey(AuthConstant.HEADER, AuthConstant.HEADER, "header");
    }

    /**
     * JWT token
     * @return
     */
    private List<RequestParameter> setHeaderToken() {
        RequestParameterBuilder tokenPar = new RequestParameterBuilder();
        List<RequestParameter> pars = new ArrayList<>();
        //.contentModel(new ModelSpecification(null,null,null,null,null,null,null)).parameterType("header").accepts(CollectionUtil.newArrayList(MediaType.APPLICATION_JSON))
        tokenPar.name(AuthConstant.HEADER).description("token").required(false).build();
        pars.add(tokenPar.build());
        return pars;
    }

    /**
     * 新增 securityContexts 保持登录状态
     */
    private List<SecurityContext> securityContexts() {
        return new ArrayList<>(
                Collections.singleton(SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.regex("^(?!auth).*$"))
                        .build())
        );
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return new ArrayList<>(
                Collections.singleton(new SecurityReference(AuthConstant.HEADER, authorizationScopes)));
    }

}
