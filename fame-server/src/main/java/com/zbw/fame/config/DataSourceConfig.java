package com.zbw.fame.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author by zzzzbw
 * @since 2021/4/4 12:38
 */
@Slf4j
@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.password}")
    private String datasourcePassword;

    @PostConstruct
    public void init() {
        try {
            Class.forName(driverClassName);
            URI uri = new URI(datasourceUrl.replace("jdbc:", ""));
            String host = uri.getHost();
            int port = uri.getPort();
            String path = uri.getPath();
            String query = uri.getQuery();
            String connectUrl = "jdbc:mysql://" + host + ":" + port + "?" + query;
            Connection connection = DriverManager.getConnection(connectUrl, datasourceUsername, datasourcePassword);
            Statement statement = connection.createStatement();
            String createSql = "CREATE DATABASE IF NOT EXISTS `" + path.replace("/", "") + "` DEFAULT CHARACTER SET = `utf8` COLLATE `utf8_general_ci`;";
            statement.executeUpdate(createSql);
            statement.close();
            connection.close();
        } catch (URISyntaxException | ClassNotFoundException | SQLException e) {
            log.error(e.getMessage(), e);
        }
    }
}
