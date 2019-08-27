package com.alibaba.excel.controller;

import org.apache.commons.collections4.bag.SynchronizedSortedBag;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * 20190826
 * 14:29
 * craeted by limboo
 **/
public class ExcelLinkHandler extends DefaultHandler {



    Map<String,String> linksMap = new HashMap();

    /**
     * 用来标识解析开始
     * */
    @Override
    public void startDocument() throws SAXException {
        System.out.println("SAX解析开始");
        super.startDocument();
    }
    /**
     * 用来遍历XML文件的开始标签
     * */
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        //遍历并打印元素的属性
        int length = attributes.getLength();
        if(length > 0){
            System.out.println(attributes.toString());
        }
    }

    /**
     * 用来遍历XML文件的结束标签
     * */
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        super.endElement(uri, localName, qName);
    }

    /**
     * 文本内容
     * */
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        super.characters(ch, start, length);
        String value = new String(ch, start, length).trim();
        if(!value.equals(""))
            System.out.print(value);
    }
    /**
     * 用来标识解析结束
     * */
    @Override
    public void endDocument() throws SAXException {
        System.out.println("SAX解析结束");
        super.endDocument();
    }

    public Map<String, String> getLinksMap() {
        return linksMap;
    }

    public void setLinksMap(Map<String, String> linksMap) {
        this.linksMap = linksMap;
    }
}
