package com.zbw.fame.config;

import com.zbw.fame.interceptor.AdminInterceptor;
import com.zbw.fame.util.FameConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * web 配置
 *
 * @author zzzzbw
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

            //拦截器
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(adminInterceptor).addPathPatterns("/api/**");
            }

            // 资源
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                String mediaResource = FILE_PROTOCOL + FameConst.USER_HOME + FameConst.FAME_HOME + FameConst.MEDIA_DIR;
                log.info("MediaResource:{}", mediaResource);

                registry.addResourceHandler(MEDIA_PATH_PATTERNS)
                        .addResourceLocations(mediaResource);
            }
        };
    }


    /**
     * 跨域处理Filter
     */
    @Bean
    public FilterRegistrationBean<Filter> registrationCorsBean() {
        Filter corsFilter = new GenericFilterBean() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                HttpServletRequest httpServletRequest = (HttpServletRequest) request;
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;

                // Set customized header
                String originHeaderValue = httpServletRequest.getHeader(HttpHeaders.ORIGIN);
                if (StringUtils.hasText(originHeaderValue)) {
                    httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, originHeaderValue);
                }
                httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, HttpHeaders.CONTENT_TYPE);
                httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS");
                httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
                httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "1800");

                if (!CorsUtils.isPreFlightRequest(httpServletRequest)) {
                    chain.doFilter(httpServletRequest, httpServletResponse);
                }
            }
        };

        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>(corsFilter);
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }
}
