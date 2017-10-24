package com.zbw.fame;

import com.zbw.fame.interceptor.FameInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * Spring boot
 *
 * @author zbw
 * @create 2017/7/5.
 */
@SpringBootApplication
@MapperScan(basePackages = "com.zbw.fame.mapper")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    //springmvc配置
    @Bean
    public WebMvcConfigurer webMvcConfigurerAdapter() {

        return new WebMvcConfigurerAdapter() {

            @Autowired
            private FameInterceptor fameInterceptor;

            //跨域请求配置
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:8010");
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
