package com.alibaba.excel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class EasyexcelApplication {
    public static void main(String[] args) {
        SpringApplication.run(EasyexcelApplication.class, args);
    }
    //为了打包springboot项目
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }

}
