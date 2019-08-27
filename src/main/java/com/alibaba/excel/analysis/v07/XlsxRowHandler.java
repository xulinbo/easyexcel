package com.alibaba.excel.analysis.v07;

import com.alibaba.excel.annotation.FieldType;
import com.alibaba.excel.constant.ExcelXmlConstants;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventRegisterCenter;
import com.alibaba.excel.event.OneRowAnalysisFinishEvent;
import com.alibaba.excel.util.PositionUtils;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.alibaba.excel.constant.ExcelXmlConstants.*;

/**
 *
 * @author jipengfei
 */
public class XlsxRowHandler extends DefaultHandler {

    private String currentCellIndex;

    private FieldType currentCellType;

    private int curRow;

    private int curCol;

    private String[] curRowContent = new String[20];

    private String currentCellValue;

    private SharedStringsTable sst;

    private AnalysisContext analysisContext;

    private AnalysisEventRegisterCenter registerCenter;

    private Map<String,String> link_map = new HashMap<String,String>();

    public XlsxRowHandler(AnalysisEventRegisterCenter registerCenter, SharedStringsTable sst,
                          AnalysisContext analysisContext) {
        this.registerCenter = registerCenter;
        this.analysisContext = analysisContext;
        this.sst = sst;

    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {

        setTotalRowCount(name, attributes);

        startCell(name, attributes);

        startCellValue(name);

    }

    private void setTotalRowCount(String name, Attributes attributes) {
        if (DIMENSION.equals(name)) {//dimension
            String d = attributes.getValue(DIMENSION_REF);  //ref
            String totalStr = d.substring(d.indexOf(":") + 1, d.length());
            String c = totalStr.toUpperCase().replaceAll("[A-Z]", "");
            analysisContext.setTotalCount(Integer.parseInt(c));
        }
        if(ExcelXmlConstants.HYPERLINK.equals(name)){
            String sheet = attributes.getValue("location");
            if (sheet != null && attributes.getValue("display")!=null) {
                sheet = sheet.replaceAll("!A1","");
                link_map.put(attributes.getValue("display"),sheet);
                System.out.println(attributes.getValue("display")+"="+sheet);
            }
        }
    }
    private void startCell(String name, Attributes attributes) {
        if (ExcelXmlConstants.CELL_TAG.equals(name)) {//C
            currentCellIndex = attributes.getValue(ExcelXmlConstants.POSITION);//r
            int nextRow = PositionUtils.getRow(currentCellIndex);
            if (nextRow > curRow) {
                curRow = nextRow;
                // endRow(ROW_TAG);
            }
            analysisContext.setCurrentRowNum(curRow);
            curCol = PositionUtils.getCol(currentCellIndex);

            String cellType = attributes.getValue("t");
            currentCellType = FieldType.EMPTY;
            if (cellType != null && cellType.equals("s")) {
                currentCellType = FieldType.STRING;
            }
        }
    }
    private void startCellValue(String name) {
        if (name.equals(CELL_VALUE_TAG) || name.equals(CELL_VALUE_TAG_1)) {//v || t
            // initialize current cell value
            currentCellValue = "";
        }
    }


    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        endRow(name);
        endCellValue(name);
        analysisContext.setLink_map(link_map);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        currentCellValue += new String(ch, start, length);
    }



    private void endRow(String name) {
        if (name.equals(ROW_TAG)) { //row
            registerCenter.notifyListeners(new OneRowAnalysisFinishEvent(curRowContent,curCol));
            curRowContent = new String[20];
        }
    }
    private void endCellValue(String name) throws SAXException {
        // ensure size
        if (curCol >= curRowContent.length) {
            curRowContent = Arrays.copyOf(curRowContent, (int)(curCol * 1.5));
        }
        if (CELL_VALUE_TAG.equals(name)) {  //v

            switch (currentCellType) {
                case STRING:
                    int idx = Integer.parseInt(currentCellValue);
                    currentCellValue = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
                    currentCellType = FieldType.EMPTY;
                    break;
            }
            curRowContent[curCol] = currentCellValue;
        } else if (CELL_VALUE_TAG_1.equals(name)) { //t
            curRowContent[curCol] = currentCellValue;
        }
    }

}

