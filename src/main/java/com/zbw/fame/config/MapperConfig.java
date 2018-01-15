package com.zbw.fame.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import java.util.Properties;

/**
 * Mapper插件Config
 *
 * @author zbw
 * @create 2018/1/19 15:27
 */
@Configuration
public class MapperConfig {

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.zbw.fame.mapper");
        Properties propertiesMapper = new Properties();
        propertiesMapper.setProperty("mappers","tk.mybatis.mapper.common.Mapper");
        mapperScannerConfigurer.setProperties(propertiesMapper);
        return mapperScannerConfigurer;
    }
}
