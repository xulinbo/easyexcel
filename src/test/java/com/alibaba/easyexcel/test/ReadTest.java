package com.alibaba.easyexcel.test;

import com.alibaba.easyexcel.test.listen.ExcelListener;
import com.alibaba.easyexcel.test.model.IndexModel;
import com.alibaba.easyexcel.test.model.ReadModel;
import com.alibaba.easyexcel.test.model.ReadModel2;
import com.alibaba.easyexcel.test.model.WriteModel;
import com.alibaba.easyexcel.test.util.FileUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.metadata.Sheet;
import org.apache.commons.collections4.bag.SynchronizedSortedBag;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReadTest {

    @Test
    public void test(){
        try {
            InputStream is = new FileInputStream("F:\\test.txt");
            byte[] bs = new byte[10240];
            StringBuffer sb = new StringBuffer();
            while(is.read(bs) != -1){
                sb.append(new String(bs));
            }
            System.out.println(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void makeup() throws IOException {
        InputStream inputStream =new BufferedInputStream(new FileInputStream("F:\\开发文档\\渤海银行整合平台服务接口清单-瘦身版.xlsx"));
        Object o = new Object();
        com.alibaba.excel.listener.ExcelListener excelListener = new com.alibaba.excel.listener.ExcelListener();
        ExcelReader excelReader = EasyExcelFactory.getReader(inputStream,excelListener);
        List<Sheet> sheets = excelReader.getSheets();
        inputStream.close();
        inputStream =new BufferedInputStream(new FileInputStream("F:\\开发文档\\渤海银行整合平台服务接口清单-瘦身版.xlsx"));
        List<Object> data = EasyExcelFactory.read(inputStream, new Sheet(1, 2));
        inputStream.close();
        List<String> indexs = new ArrayList<String>();//清单页交易码
        for(Object obj:data){
           List<String> list = (List<String>) obj;
            indexs.add(list.get(0));
        }

        List<String> sheetsname = new ArrayList<String>();//sheetname
        sheets = sheets.subList(7,sheets.size());
        for(Sheet sheet:sheets){
            sheetsname.add(sheet.getSheetName());
        }

        List<String> sheetnames = new ArrayList<String>();
        for(String name : sheetsname){
            if(!indexs.contains(name)){
                sheetnames.add(name);
            }
        }

        List<String> indexsname = new ArrayList<String>();
        for(String name:indexs){
            if(!sheetsname.contains(name)){
                indexsname.add(name);
            }
        }

        List<String> result = new ArrayList<String>();//清单页中 不与sheet匹配的
        for(String name : indexsname){
            if(!sheetnames.contains(name)){
                name = name.substring(name.indexOf(".")+1);
                if(!sheetnames.contains(name)){
                    System.out.println(name);
                    result.add(name);
                }
            }
        }



    }

    /**
     * 07版本excel读数据量少于1千行数据，内部采用回调方法.
     *
     * @throws IOException 简单抛出异常，真实环境需要catch异常,同时在finally中关闭流
     */
    @Test
    public void simpleReadListStringV2007() throws IOException {
        System.out.println(new WriteModel().toString());
        InputStream inputStream = FileUtil.getResourcesFileInputStream("test.xlsx");
        List<Object> data = EasyExcelFactory.read(inputStream, new Sheet(10, 4));
        inputStream.close();
        print(data);
    }


    /**
     * 07版本excel读数据量少于1千行数据自动转成javamodel，内部采用回调方法.
     *
     * @throws IOException 简单抛出异常，真实环境需要catch异常,同时在finally中关闭流
     */
    @Test
    public void simpleReadJavaModelV2007() throws IOException {
        InputStream inputStream = FileUtil.getResourcesFileInputStream("2007.xlsx");
        List<Object> data = EasyExcelFactory.read(inputStream, new Sheet(2, 1, ReadModel.class));
        inputStream.close();
        print(data);
    }

    /**
     * 07版本excel读数据量大于1千行，内部采用回调方法.
     *
     * @throws IOException 简单抛出异常，真实环境需要catch异常,同时在finally中关闭流
     */
    @Test
    public void saxReadListStringV2007() throws IOException {

        InputStream inputStream = FileUtil.getResourcesFileInputStream("test.xlsx");
        ExcelListener excelListener = new ExcelListener();
        EasyExcelFactory.readBySax(inputStream, new Sheet(1, 1), excelListener);
        List<Object> list = excelListener.getData();
        for(Object obj:list){
            System.out.println(obj);
        }
        inputStream.close();

    }
    /**
     * 07版本excel读数据量大于1千行，内部采用回调方法.
     *
     * @throws IOException 简单抛出异常，真实环境需要catch异常,同时在finally中关闭流
     */
    @Test
    public void saxReadJavaModelV2007() throws IOException {
        InputStream inputStream = FileUtil.getResourcesFileInputStream("2007.xlsx");
        ExcelListener excelListener = new ExcelListener();
        EasyExcelFactory.readBySax(inputStream, new Sheet(2, 1, ReadModel.class), excelListener);
        inputStream.close();
    }

    /**
     * 07版本excel读取sheet
     *
     * @throws IOException 简单抛出异常，真实环境需要catch异常,同时在finally中关闭流
     */
    @Test
    public void saxReadSheetsV2007() throws IOException {
        InputStream inputStream = FileUtil.getResourcesFileInputStream("test.xlsx");
//        InputStream inputStream =new FileInputStream("/src/test/resources/test.xlsx");
        ExcelListener excelListener = new ExcelListener();
        ExcelReader excelReader = EasyExcelFactory.getReader(inputStream,excelListener);
        List<Sheet> sheets = excelReader.getSheets();
        for (Sheet sheet:sheets) {
            if(sheet.getSheetNo() ==1) {
                excelReader.read(sheet);
            }else if(sheet.getSheetNo() ==2){
                sheet.setHeadLineMun(1);
                sheet.setClazz(ReadModel.class);
                excelReader.read(sheet);
            }else if(sheet.getSheetNo() ==3){
                sheet.setHeadLineMun(1);
                sheet.setClazz(ReadModel2.class);
                excelReader.read(sheet);
            }
        }
        inputStream.close();
    }



    /**
     * 03版本excel读数据量少于1千行数据，内部采用回调方法.
     *
     * @throws IOException 简单抛出异常，真实环境需要catch异常,同时在finally中关闭流
     */
    @Test
    public void simpleReadListStringV2003() throws IOException {
        InputStream inputStream = FileUtil.getResourcesFileInputStream("2003.xls");
        List<Object> data = EasyExcelFactory.read(inputStream, new Sheet(1, 0));
        inputStream.close();
        print(data);
    }

    /**
     * 03版本excel读数据量少于1千行数据转成javamodel，内部采用回调方法.
     *
     * @throws IOException 简单抛出异常，真实环境需要catch异常,同时在finally中关闭流
     */
    @Test
    public void simpleReadJavaModelV2003() throws IOException {
        InputStream inputStream = FileUtil.getResourcesFileInputStream("2003.xls");
        List<Object> data = EasyExcelFactory.read(inputStream, new Sheet(2, 1, ReadModel.class));
        inputStream.close();
        print(data);
    }

    /**
     * 03版本excel读数据量大于1千行数据，内部采用回调方法.
     *
     * @throws IOException 简单抛出异常，真实环境需要catch异常,同时在finally中关闭流
     */
    @Test
    public void saxReadListStringV2003() throws IOException {
        InputStream inputStream = FileUtil.getResourcesFileInputStream("2003.xls");
        ExcelListener excelListener = new ExcelListener();
        EasyExcelFactory.readBySax(inputStream, new Sheet(2, 1), excelListener);
        inputStream.close();
    }

    /**
     * 03版本excel读数据量大于1千行数据转成javamodel，内部采用回调方法.
     *
     * @throws IOException 简单抛出异常，真实环境需要catch异常,同时在finally中关闭流
     */
    @Test
    public void saxReadJavaModelV2003() throws IOException {
        InputStream inputStream = FileUtil.getResourcesFileInputStream("2003.xls");
        ExcelListener excelListener = new ExcelListener();
        EasyExcelFactory.readBySax(inputStream, new Sheet(2, 1, ReadModel.class), excelListener);
        inputStream.close();
    }

    /**
     * 00版本excel读取sheet
     *
     * @throws IOException 简单抛出异常，真实环境需要catch异常,同时在finally中关闭流
     */
    @Test
    public void saxReadSheetsV2003() throws IOException {
        InputStream inputStream = FileUtil.getResourcesFileInputStream("2003.xls");
        ExcelListener excelListener = new ExcelListener();
        ExcelReader excelReader = EasyExcelFactory.getReader(inputStream,excelListener);
        List<Sheet> sheets = excelReader.getSheets();
        System.out.println();
        for (Sheet sheet:sheets) {
            if(sheet.getSheetNo() == 1) {
                excelReader.read(sheet);
            }else {
                sheet.setHeadLineMun(2);
                sheet.setClazz(ReadModel.class);
                excelReader.read(sheet);
            }
        }
        inputStream.close();
    }


    public void print(List<Object> datas){
        int i=0;
        for (Object ob:datas) {
            System.out.println(i++);
            System.out.println(ob.toString());
        }
    }

}
