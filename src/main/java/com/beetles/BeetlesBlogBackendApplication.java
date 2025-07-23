package com.beetles;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.beetles.mapper")
public class BeetlesBlogBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeetlesBlogBackendApplication.class, args);
    }

}
