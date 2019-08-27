package com.alibaba.excel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class ExcelConfig {

    @Bean(initMethod = "init")
    public IndexDataMap indexDataMap(){
        IndexDataMap indexDataMap = new IndexDataMap();
        indexDataMap.setPath("/Users/linyu/mywork/ecm/excel/ecmexcel.xlsx");
//        indexDataMap.setPath("F:\\开发文档\\渤海银行整合平台服务接口清单-瘦身版.xlsx");
        return indexDataMap;
    }

}
