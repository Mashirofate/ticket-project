package com.tickets.config;


import com.tickets.interceptor.AuthorityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * web 配置类
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    /**
     * 启动 websoket任务
     * @return
     */
//    @Bean
//    public ServerEndpointExporter serverEndpointExporter() {
//        return new ServerEndpointExporter();
//    }


    @Autowired
    private AuthorityInterceptor authorityInterceptor;

    /**
     * 解决前后端跨域问题
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //添加映射路径
        registry.addMapping("/**")
                //是否发送Cookie
                .allowCredentials(true)
                //设置放行哪些原始域   SpringBoot2.4.4下低版本使用.allowedOrigins("*")
                .allowedOriginPatterns("*")
                //放行哪些请求方式  .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                //.allowedMethods(new String[]{"GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS"})
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                //.allowedMethods("*") //或者放行全部
                .allowedHeaders("*")
                //暴露哪些原始请求头部信息
                .exposedHeaders("*")
                .maxAge(36000);

    }

    /**
     * 配置拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 无需拦截的接口集合  静态资源放行
        List<String> ignorePath = new ArrayList<>();
        // swagger
        ignorePath.add("/swagger-resources/**");
        ignorePath.add("/doc.html");
        ignorePath.add("/v3/**");
        ignorePath.add("/webjars/**");
        ignorePath.add("/springdoc/**");
        ignorePath.add("/static/**");
        ignorePath.add("/templates/**");
        ignorePath.add("/error");
        ignorePath.add("/cipher/check");
        ignorePath.add("/manager/login");
        ignorePath.add("/swagger-ui.html");
        ignorePath.add("/socket/**");
        ignorePath.add("/tasks/**");
        //先拦截认证，再拦截授权
        registry.addInterceptor(authorityInterceptor).addPathPatterns("/**").excludePathPatterns(ignorePath);
        //registry.addInterceptor(authorityInterceptor).addPathPatterns("/**");

    }


}
