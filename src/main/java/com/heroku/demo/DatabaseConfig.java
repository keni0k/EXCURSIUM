package com.heroku.demo;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application.properties")
public class DatabaseConfig {
    @Bean (name = "dataSource")
    @Primary
    @ConfigurationProperties("classpath:application.properties")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }
}