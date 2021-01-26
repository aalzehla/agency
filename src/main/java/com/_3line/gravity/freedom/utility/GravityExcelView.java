package com._3line.gravity.freedom.utility;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GravityExcelView extends AbstractXlsView {

    @SuppressWarnings("unchecked")
    protected void buildExcelDocument(Map<String, Object> model,
                                      HSSFWorkbook workbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response)
    {

    }

    @Override
    protected void buildExcelDocument(Map<String, Object> map, Workbook workbook, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        //VARIABLES REQUIRED IN MODEL
        String sheetName = (String)map.get("sheetname");
        List<String> headers = (List<String>)map.get("headers");
        List<List<String>> results = (List<List<String>>)map.get("results");
        List<String> numericColumns = new ArrayList<String>();
        if (map.containsKey("numericcolumns"))
            numericColumns = (List<String>)map.get("numericcolumns");
        //BUILD DOC
        //
        Sheet sheet = workbook.createSheet(sheetName);
        sheet.setDefaultColumnWidth((short) 12);
        int currentRow = 0;
        short currentColumn = 0;
        //CREATE STYLE FOR HEADER
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerStyle.setFont(headerFont);
        //POPULATE HEADER COLUMNS
        Row headerRow = sheet.createRow(currentRow);
        for(String header:headers){
            RichTextString text = new HSSFRichTextString(header);
            Cell cell = headerRow.createCell(currentColumn);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(text);
            currentColumn++;
        }
        //POPULATE VALUE ROWS/COLUMNS
        currentRow++;//exclude header
        for(List<String> result: results){
            currentColumn = 0;
            Row row = sheet.createRow(currentRow);
            for(String value : result){//used to count number of columns
                Cell cell = row.createCell(currentColumn);
                if (numericColumns.contains(headers.get(currentColumn))){
                    cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(value);
                } else {
                    HSSFRichTextString text = new HSSFRichTextString(value);
                    cell.setCellValue(text);
                }
                currentColumn++;
            }
            currentRow++;
        }
    }
}
