package com.zbw.fame.config;

import com.zbw.fame.interceptor.AdminInterceptor;
import com.zbw.fame.util.FameConsts;
import com.zbw.fame.util.FameUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.URI;
import java.nio.file.FileSystems;

/**
 * web 配置
 *
 * @author zbw
 * @since 2017/12/19 11:11
 */
@Slf4j
@Configuration
public class WebConfig {

    private static final String FILE_PROTOCOL = "file:///";

    private static final String MEDIA_PATH_PATTERNS = "/media/**";

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Autowired
            private AdminInterceptor adminInterceptor;

            //跨域请求配置
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowCredentials(true)
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE");
            }

            //拦截器
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(adminInterceptor).addPathPatterns("/api/**");
            }

            // 资源
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                String mediaResource = FILE_PROTOCOL + FameConsts.USER_HOME + FameConsts.FAME_HOME + FameConsts.MEDIA_DIR;
                log.info("MediaResource:{}", mediaResource);

                registry.addResourceHandler(MEDIA_PATH_PATTERNS)
                        .addResourceLocations(mediaResource);
            }
        };
    }
}
