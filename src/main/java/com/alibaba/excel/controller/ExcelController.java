package com.alibaba.excel.controller;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.listener.ExcelListener;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.swing.text.html.parser.Entity;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
@RestController
public class ExcelController {

    @Autowired
    IndexDataMap indexDataMap;

    @RequestMapping("/index")
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("queryList");
        mv.addObject("Modules",indexDataMap.getModules());
        mv.addObject("Transactions",indexDataMap.getModuleTransMap());
        mv.addObject("Test","test");
        return mv;
    }

    @RequestMapping("/query")
    public ModelAndView getTransFields(String module,String code,String name) throws IOException {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("queryList");

        InputStream inputStream =new FileInputStream(indexDataMap.getPath());
        ExcelListener excelListener = new ExcelListener();
        ExcelReader excelReader = EasyExcelFactory.getReader(inputStream,excelListener);
        String sheetname = indexDataMap.getCodeMap().get(code);
        if(sheetname == null){
            mv.setStatus(HttpStatus.NO_CONTENT);
            return mv;
        }
        Sheet sheet = excelReader.getSheetByName(sheetname);
        if(sheet == null ){
            mv.setStatus(HttpStatus.NOT_FOUND);
            return mv;
        }
        excelReader.read(sheet);
        List<Object> list = excelListener.getData();
        if(list == null || list.size()==0){
            mv.setStatus(HttpStatus.NO_CONTENT);
            return mv;
        }
//        [接口名：, null, fund.MCAssembleInvestPrdSetQry, null, null, null, null, null]
//        [接口中文名：, null, CPPI组合投资产品设置查询, null, null, null, null, null]
        String transCode = ((List<String>) list.get(0)).get(2);
        String transName = ((List<String>) list.get(1)).get(2);

        list = list.subList(4,list.size());
        boolean request = true;
        List<List<String>> reqList = new ArrayList<List<String>>();
        List<List<String>> resList = new ArrayList<List<String>>();
        List<String> cellList ;
        for(int i = 0;i<list.size();i++){
            cellList = (List<String>) list.get(i);
            String a = cellList.get(0);
            if(a != null && !a.equals("")){
                if(a.equals("响应报文")){
                    i++;
                    request = false;
                    continue;
                }
                if(a.equals("返回")){
                    break;
                }
                if(request){
                    reqList.add(cellList);
                }else {
                    resList.add(cellList);
                }
            }
        }
        inputStream.close();

        mv.addObject("ReqList",reqList);
        mv.addObject("ResList",resList);
        mv.addObject("TransCode",transCode);
        mv.addObject("TransName",transName);

        return mv;
    }
}
