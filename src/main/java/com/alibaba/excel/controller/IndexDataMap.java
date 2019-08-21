package com.alibaba.excel.controller;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;


public class IndexDataMap {

    private String path;
    private Map<String,String> codeMap = new HashMap<String, String>(); //交易名与sheet页名的映射
    private Map<String,TransModel> transMap = new HashMap<String, TransModel>(); //交易名与 row
    private Map<String,String> nameMap = new HashMap<String, String>(); //英文名与中文名的映射
    private Map<String,List<String>> moduleMap = new HashMap<String,List<String>>(); //模块与交易名
    private Map<String,List<TransModel>> moduleTransMap = new HashMap<String,List<TransModel>>(); //模块与row
    private List<String> modules = new ArrayList<String>();//模块

    public void init() throws IOException {
        InputStream inputStream =new BufferedInputStream(new FileInputStream(path));
        SXSSFWorkbook wb = new SXSSFWorkbook(new XSSFWorkbook(inputStream));
        inputStream.close();
        int num = wb.getNumberOfSheets();
        XSSFSheet xsheet =wb.getXSSFWorkbook().getSheetAt(0);
        Iterator<Row> it = xsheet.rowIterator();
        while(it.hasNext()){
            it.next();it.next();
            XSSFRow row = (XSSFRow) it.next();
            XSSFCell cell = row.getCell(0);
            String code = cell.getStringCellValue();//*!A1
            String module = row.getCell(1).getStringCellValue();
            String name_cn = row.getCell(2).getStringCellValue();
            XSSFHyperlink hyperlink = cell.getHyperlink();
            if(hyperlink == null || hyperlink.getAddress() == null){
                System.out.println(code);
                continue;
            }
            String add = cell.getHyperlink().getAddress();
            if(add.indexOf("!A1")>=0)add = add.substring(0,add.indexOf("!A1"));

            if(!module.contains(module))modules.add(module);//模块列表

            nameMap.put(code,name_cn);// code 与中文名
            if(moduleMap.containsKey(module)){
                moduleMap.get(module).add(code);
            }else{
                List<String> list = new ArrayList<String>();
                list.add(code);
                moduleMap.put(module,list);
            }

            codeMap.put(code,add);//code 与 sheet
            TransModel transModel = new TransModel(code,name_cn,add,module);
            transMap.put(code,transModel);
            if(moduleTransMap.containsKey(module)){
                moduleTransMap.get(module).add(transModel);
            }else{
                List<TransModel> trans = new ArrayList<TransModel>();
                trans.add(transModel);
                moduleTransMap.put(module,trans);
            }
        }
        if(modules.size()>0) modules.sort(null);
    }
    class TransModel{
        private String code;
        private String name;
        private String add;
        private String module;
        public TransModel(){

        }
        public TransModel(String code, String name, String add, String module) {
            this.code = code;
            this.name = name;
            this.add = add;
            this.module = module;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAdd() {
            return add;
        }

        public void setAdd(String add) {
            this.add = add;
        }

        public String getModule() {
            return module;
        }

        public void setModule(String module) {
            this.module = module;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, String> getCodeMap() {
        return codeMap;
    }

    public void setCodeMap(Map<String, String> codeMap) {
        this.codeMap = codeMap;
    }

    public Map<String, String> getNameMap() {
        return nameMap;
    }

    public void setNameMap(Map<String, String> nameMap) {
        this.nameMap = nameMap;
    }

    public Map<String, List<String>> getModuleMap() {
        return moduleMap;
    }

    public void setModuleMap(Map<String, List<String>> moduleMap) {
        this.moduleMap = moduleMap;
    }

    public List<String> getModules() {
        return modules;
    }

    public void setModules(List<String> modules) {
        this.modules = modules;
    }

    public Map<String, TransModel> getTransMap() {
        return transMap;
    }

    public void setTransMap(Map<String, TransModel> transMap) {
        this.transMap = transMap;
    }

    public Map<String, List<TransModel>> getModuleTransMap() {
        return moduleTransMap;
    }

    public void setModuleTransMap(Map<String, List<TransModel>> moduleTransMap) {
        this.moduleTransMap = moduleTransMap;
    }
}
