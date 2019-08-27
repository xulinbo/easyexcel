package com.alibaba.excel.controller;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.analysis.ExcelAnalyser;
import com.alibaba.excel.analysis.ExcelAnalyserImpl;
import com.alibaba.excel.analysis.v07.XlsxSaxAnalyser;
import com.alibaba.excel.listener.ExcelListener;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.util.FileUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
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
    private List<String> codes = new ArrayList<String>();
    private List<String> names = new ArrayList<String>();
    private List<String> code_namecn = new ArrayList<String>();//code;name
    private Map<String,List<String>> module_code_namecnMap=new HashMap<String,List<String>>();//模块 code;name


    public void init() throws IOException,XmlException {

        InputStream inputStream =new BufferedInputStream(new FileInputStream(path));
//        List<Object> data = EasyExcelFactory.read(inputStream, new Sheet(1, 2));
        ExcelListener excelListener = new ExcelListener();
        ExcelReader excelReader = EasyExcelFactory.getReader(inputStream,excelListener);
        excelReader.read(new Sheet(1, 2));
        codeMap = excelReader.getAnalyser().getAnalysisContext().getLink_map();//超链接
        List<Object> data = excelListener.getData();
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
            codes.add(code);
            names.add(name_cn);
            String code_name = code+";"+name_cn;
            code_namecn.add(code_name);
            if(module_code_namecnMap.containsKey(module)){
                module_code_namecnMap.get(module).add(code_name);
            }else{
                List<String> code_names = new ArrayList<>();
                code_names.add(code_name);
                module_code_namecnMap.put(module,code_names);
            }
        }
        //模块 排序
        if(modules.size()>0) modules.sort(null);
    }

    /**
     * 解析  生成超链接信息
     * @param ins
     * @throws IOException
     */
    private Map parse(InputStream ins) throws IOException {
        SAXParserFactory sFactory = SAXParserFactory.newInstance();
        try{
            SAXParser saxParser  = sFactory.newSAXParser();
            ExcelLinkHandler excelLinkHandler = new ExcelLinkHandler();
            saxParser.parse(ins,excelLinkHandler);
            codeMap = excelLinkHandler.getLinksMap();
            return codeMap;
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }finally {
            if(ins != null )ins.close();
        }
        return codeMap;
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
    public List<String> getCodes() {
        return codes;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    public List<String> getCode_namecn() {
        return code_namecn;
    }

    public void setCode_namecn(List<String> code_namecn) {
        this.code_namecn = code_namecn;
    }

    public Map<String, List<String>> getModule_code_namecnMap() {
        return module_code_namecnMap;
    }

    public void setModule_code_namecnMap(Map<String, List<String>> module_code_namecnMap) {
        this.module_code_namecnMap = module_code_namecnMap;
    }

}
