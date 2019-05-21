package com.zbw.fame.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * springboot初始化完成后执行的动作
 *
 * @author zbw
 * @since 2019/05/18 16:54
 */
@Slf4j
@Component
public class InitApplicationRunner implements ApplicationRunner {

    /**
     * 用于初始化访问的链接
     */
    private static final String INIT_URL = "/api/article";

    @Value("${server.port}")
    private String port;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Initializing Fame after springboot loading completed...");
        long startTime = System.currentTimeMillis();
        InetAddress address;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            log.error("Get InetAddress error!", e);
            return;
        }

        // 任意访问一个url，使DispatcherServlet和数据库连接初始化
        String url = "http://" + address.getHostAddress() + ":" + port + INIT_URL;
        log.info("The url for init: {}", url);

        try {
            new RestTemplate().getForObject(url, String.class);
        } catch (Exception e) {
            log.error("InitApplicationRunner error", e);
        }
        log.info("Fame initialization in " + (System.currentTimeMillis() - startTime) + " ms");
    }
}
