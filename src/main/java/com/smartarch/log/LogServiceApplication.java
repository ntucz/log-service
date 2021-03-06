package com.smartarch.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.smartarch.platform.EnableCommon;

@EnableCommon
@EnableElasticsearchRepositories(basePackages = "com.smartarch.log.dao")
@SpringBootApplication
public class LogServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogServiceApplication.class, args);
    }

}
