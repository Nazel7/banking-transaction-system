package com.sankore.bank.configs;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class JOOQDBConfig {

    @Value("${spring.datasource.username}")
    private String dbUserName;

    @Value("${spring.datasource.password}")
    private String dbPasscode;

    @Value("${spring.datasource.url}")
    private String dbUrl;


    @Bean
    public DSLContext connect() throws SQLException {

        Connection conn = DriverManager.getConnection(dbUrl, dbUserName, dbPasscode);

        return DSL.using(conn, SQLDialect.POSTGRES);

    }

}
