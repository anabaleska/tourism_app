package com.example.tourism_app.config;

import org.hibernate.community.dialect.SQLiteDialect;
import org.hibernate.dialect.Dialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfig {

    @Bean
    public Dialect hibernateDialect() {
        return new SQLiteDialect();
    }
}