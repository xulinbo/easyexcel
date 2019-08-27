package com.alibaba.excel.controller;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.listener.ExcelListener;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.util.FileUtil;
import org.apache.xmlbeans.impl.regex.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.swing.text.html.parser.Entity;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class ExcelController {

    @Autowired
    IndexDataMap indexDataMap;

    @RequestMapping("/index")
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("queryList");
        mv.addObject("Modules",indexDataMap.getModules());

        String module = indexDataMap.getModules().get(0);
        mv.addObject("TransList",indexDataMap.getModuleTransMap().get(module));
        return mv;
    }

    @RequestMapping("/query")
    public ModelAndView getTransFields(@RequestParam(name="module",required = false) String module,
                                       @RequestParam(name="code",required = false) String code,
                                       @RequestParam(name="name",required = false)String name) throws IOException {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("queryList");
        mv.addObject("Modules",indexDataMap.getModules());
        //符合预期的code集合
        List<String> result_codeList=new ArrayList<>();
        boolean moduleflag = false;
        if(module != null && module.length()>0){
            moduleflag = true;
            result_codeList.addAll(indexDataMap.getModule_code_namecnMap().get(module));
        }
        //正则匹配 name
        if(name != null && name.length()>0){
            result_codeList = matchCode(moduleflag,name,module,result_codeList);
            mv.addObject("name",name);
        }
        //正则匹配 code
        if (code != null && code.length() > 0) {
            //模糊 不区分大小写
            result_codeList =  matchCode(moduleflag,code,module,result_codeList);
            mv.addObject("code",code);
        }
        List<IndexDataMap.TransModel> transList = new ArrayList<>();
        if(result_codeList.size()>0){
            result_codeList.sort(null);
            for(String str:result_codeList){
                transList.add(indexDataMap.getTransMap().get(str.split(";")[0]));
            }
        }
        if(transList.size()==0){
            mv.addObject("ErrorMsg","查询无记录");
        }else{
            mv.addObject("TransList",transList);
        }
        return mv;
    }

    @RequestMapping("/queryDetail")
    public ModelAndView getTransFields(String code) throws IOException {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("queryList");
        mv.addObject("Modules",indexDataMap.getModules());

        String sheetname = indexDataMap.getCodeMap().get(code);
        if (sheetname == null) {
            mv.setStatus(HttpStatus.NO_CONTENT);
            mv.addObject("ErrorMsg","查询无记录");
            return mv;
        }
        //使用easyexcel读取  数据存储在listener
        InputStream inputStream =new BufferedInputStream(new FileInputStream(indexDataMap.getPath()));
        ExcelListener excelListener = new ExcelListener();
        ExcelReader excelReader = EasyExcelFactory.getReader(inputStream, excelListener);
        Sheet sheet = excelReader.getSheetByName(sheetname);
        if (sheet == null) {
            mv.setStatus(HttpStatus.NOT_FOUND);
            mv.addObject("ErrorMsg","查询无记录");
            return mv;
        }
        //使用easyexcel读取  数据存储在listener
        excelReader.read(sheet);
        inputStream.close();
        List<Object> list = excelListener.getData();
        if (list == null || list.size() == 0) {
            mv.setStatus(HttpStatus.NO_CONTENT);
            mv.addObject("ErrorMsg","查询无记录");
            return mv;
        }
//        [接口名：, null, fund.MCAssembleInvestPrdSetQry, null, null, null, null, null]
//        [接口中文名：, null, CPPI组合投资产品设置查询, null, null, null, null, null]
        String transCode = ((List<String>) list.get(0)).get(2);
        if (transCode == null || "".equals(transCode)) {
            transCode = ((List<String>) list.get(0)).get(1);
        }
        String transName = ((List<String>) list.get(1)).get(2);
        if (transName == null || "".equals(transName)) {
            transName = ((List<String>) list.get(1)).get(1);
        }
        list = list.subList(4, list.size());
        boolean request = true;
        List<List<String>> reqList = new ArrayList<List<String>>();
        List<List<String>> resList = new ArrayList<List<String>>();
        List<String> cellList;
        for (int i = 0; i < list.size(); i++) {
            cellList = (List<String>) list.get(i);
            String a = cellList.get(0);
            if (a != null && !a.equals("")) {
                if (a.equals("响应报文")) {
                    i++;
                    request = false;
                    continue;
                }
                if (a.equals("返回")) {
                    break;
                }
                if (request) {
                    reqList.add(cellList);
                } else {
                    resList.add(cellList);
                }
            }
        }
        mv.addObject("ReqList", reqList);
        mv.addObject("ResList", resList);
        mv.addObject("TransCode", transCode);
        mv.addObject("TransName", transName);

        return mv;
    }

    /**
     * 正则匹配 code
     * itList ----{code;中文名 , ......}
     * @param moduleflag
     * @param code
     * @param module
     * @param result_codeList
     * @return
     */
    public List matchCode(boolean moduleflag,String code,String module,List<String> result_codeList){
        List<String> itList = moduleflag?indexDataMap.getModule_code_namecnMap().get(module):indexDataMap.getCode_namecn();
        if(result_codeList.size()>0){
            itList = result_codeList;
            result_codeList=new ArrayList<String>();
        }
        for(String str : itList){
            Pattern p = Pattern.compile(code,Pattern.CASE_INSENSITIVE);
            Matcher matcher = p.matcher(str);
            if(matcher.find()){
//            if( Pattern.matches("(?!)"+".*"+code+".*",str)){
                result_codeList.add(str);
            }
        }
        return result_codeList;
    }

}
