package com.alibaba.easyexcel.test.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

/**
 * 返回的超级链接
 */
public class HyperlinkModel extends BaseRowModel {
    public static int headsize=1;
    @ExcelProperty(value = {"以下是循环体List"},index = 0)
    private String p0;

    public HyperlinkModel(){
        super();

    }


}
