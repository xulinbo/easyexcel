package com.alibaba.easyexcel.test.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

public class ListModel extends BaseRowModel {
    public static int headsize=1;
    @ExcelProperty(value = {"以下是循环体List"},index = 0)
    private String p0;
}
