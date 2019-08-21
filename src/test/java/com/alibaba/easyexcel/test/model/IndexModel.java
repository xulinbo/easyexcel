package com.alibaba.easyexcel.test.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import java.math.BigDecimal;
import java.util.Date;

public class IndexModel extends BaseRowModel {

    public static int headsize=4;

    @ExcelProperty(value = {"接口名：","接口中文名：","请求报文","字段名Field Nam"},index = 0)
    private String p0;

    @ExcelProperty(value = {"接口名：","接口中文名：","请求报文","字段描述Field Desc"},index = 1)
    private String p1;

    @ExcelProperty(value = {"","","","类型Type"},index = 2)
    private String p2;

    @ExcelProperty(value = {"","","","长度Length"},index = 3)
    private String p3;

    @ExcelProperty(value = {"","","","必输/可选M/O"},index = 4)
    private String p4;

    @ExcelProperty(value = {"","","","取值范围或格式Range/Format"},index = 5)
    private String p5;

    @ExcelProperty(value = {"","","","业务规则Business rules"},index = 6)
    private String p6;

    @ExcelProperty(value = {"","","","备注"},index = 7)
    private String p7;

    @Override
    public String toString() {
        return "IndexModel{" +
                "p0='" + p0 + '\'' +
                ", p1='" + p1 + '\'' +
                ", p2='" + p2 + '\'' +
                ", p3='" + p3 + '\'' +
                ", p4='" + p4 + '\'' +
                ", p5='" + p5 + '\'' +
                ", p6='" + p6 + '\'' +
                ", p7='" + p7 + '\'' +
                '}';
    }
}
