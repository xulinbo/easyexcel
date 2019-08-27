package com.alibaba.excel.analysis;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.Sheet;

import java.io.InputStream;
import java.util.List;

/**
 * Excel file analyser
 *
 * @author jipengfei
 */
public interface ExcelAnalyser {

    /**
     * parse one sheet
     *
     * @param sheetParam
     */
    void analysis(Sheet sheetParam);

    /**
     * parse all sheets
     */
    void analysis();

    /**
     * get all sheet of workbook
     *
     * @return all sheets
     */
    List<Sheet> getSheets();


    /**
     * 获取inpustream
     * @param index
     * @return
     */
    InputStream getInpuStream(int index);

    /**
     * 获取context
     * @return
     */
    AnalysisContext getAnalysisContext();

}
