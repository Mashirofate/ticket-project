package com.tickets;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
/**
 * 接口文档注解
 */

@MapperScan("com.tickets.mapper")
@SpringBootApplication
@EnableScheduling
@EnableRetry
public class TicketsApplication {
    public static void main(String[] args) {
        SpringApplication.run(TicketsApplication.class, args);
    }
}
