package com.zbw.fame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * Spring boot
 *
 * @author zzzzbw
 * @since 2017/7/5.
 */
@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
