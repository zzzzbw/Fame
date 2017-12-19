package com.zbw.fame;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


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
}
