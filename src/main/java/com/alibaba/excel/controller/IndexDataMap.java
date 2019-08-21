package com.alibaba.excel.controller;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.util.FileUtil;
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

        InputStream is = FileUtil.getResourcesFileInputStream("codeadd.properties");
        Properties properties = new Properties();
        properties.load(is);
        Set<Map.Entry<Object,Object>> set = properties.entrySet();
        Iterator<Map.Entry<Object,Object>> it = set.iterator();
        while(it.hasNext()){
            Map.Entry<Object,Object> enty= it.next();
            if(enty.getKey() != null && enty.getValue()!=null)
                codeMap.put(enty.getKey().toString(),enty.getValue().toString());
        }
        is.close();

        InputStream inputStream =new BufferedInputStream(new FileInputStream(path));
        List<Object> data = EasyExcelFactory.read(inputStream, new Sheet(1, 2));
        inputStream.close();
        for(Object obj:data){
            List<String> list = (List<String>) obj;
            String code = list.get(0);
            String module = list.get(1);
            String name_cn = list.get(2);
            String add = codeMap.get(code);//超链接
            if(add == null || "".equals(add)){
                continue;
            }
            nameMap.put(code,name_cn);// code 与中文名
            if(moduleMap.containsKey(module)){
                moduleMap.get(module).add(code);
            }else{
                List<String> modulelist = new ArrayList<String>();
                modulelist.add(code);
                moduleMap.put(module,modulelist);
            }

            TransModel transModel = new TransModel(code,name_cn,add,module);
            transMap.put(code,transModel);
            if(moduleTransMap.containsKey(module)){
                moduleTransMap.get(module).add(transModel);
            }else{
                List<TransModel> trans = new ArrayList<TransModel>();
                trans.add(transModel);
                moduleTransMap.put(module,trans);
            }
            if(!modules.contains(module)){
                modules.add(module);
            }
        }
        //模块 排序
        if(modules.size()>0) modules.sort(null);
//        SXSSFWorkbook wb = new SXSSFWorkbook(new XSSFWorkbook(inputStream));
//        inputStream.close();
//        int num = wb.getNumberOfSheets();
//        XSSFSheet xsheet =wb.getXSSFWorkbook().getSheetAt(0);
//        Iterator<Row> it = xsheet.rowIterator();
//        while(it.hasNext()){
//            it.next();it.next();
//            XSSFRow row = (XSSFRow) it.next();
//            XSSFCell cell = row.getCell(0);
//            String code = cell.getStringCellValue();
//            String module = row.getCell(1).getStringCellValue();
//            String name_cn = row.getCell(2).getStringCellValue();
//            XSSFHyperlink hyperlink = cell.getHyperlink();
//            if(hyperlink == null || hyperlink.getAddress() == null){
//                System.out.println(code);
//                continue;
//            }
//
//            String add = cell.getHyperlink().getAddress();//*!A1
//            if(add.indexOf("!A1")>=0)add = add.substring(0,add.indexOf("!A1"));
//            if(!module.contains(module))modules.add(module);//模块列表
//            nameMap.put(code,name_cn);// code 与中文名
//            if(moduleMap.containsKey(module)){
//                moduleMap.get(module).add(code);
//            }else{
//                List<String> list = new ArrayList<String>();
//                list.add(code);
//                moduleMap.put(module,list);
//            }
//            System.out.println(code+"="+add);
//            codeMap.put(code,add);//code 与 sheet
//            TransModel transModel = new TransModel(code,name_cn,add,module);
//            transMap.put(code,transModel);
//            if(moduleTransMap.containsKey(module)){
//                moduleTransMap.get(module).add(transModel);
//            }else{
//                List<TransModel> trans = new ArrayList<TransModel>();
//                trans.add(transModel);
//                moduleTransMap.put(module,trans);
//            }
//            if(modules.size()>0) modules.sort(null);
//        }

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
