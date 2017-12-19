package com.zbw.fame.config;

import com.zbw.fame.interceptor.FameInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * web 配置
 *
 * @author zbw
 * @create 2017/12/19 11:11
 */
@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer webMvcConfigurerAdapter() {
        return new WebMvcConfigurerAdapter() {
            @Autowired
            private FameInterceptor fameInterceptor;

            //跨域请求配置
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
                registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE");
            }

            //拦截器
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(fameInterceptor).addPathPatterns("/**");
            }

        };
    }
}
