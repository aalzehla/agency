/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.utility;

import com._3line.gravity.freedom.commisions.models.GravityDailyTotalCommission;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.opencsv.CSVWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.MultiValueMap;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author NiyiO
 */
public class ExportUtility {


//    private static List<String[]> mineData(MultiValueMap<String, Object> param) {
//
//        String[] headers = fetchHeaders(param);
//
//        String currentPage = "";
//        List<Object> ObjcurrentPage = param.get("currentPage");
//        if (ObjcurrentPage != null) {
//            currentPage = param.get("currentPage").get(0) + "";
//        }
//
//        List<String[]> data = new ArrayList<>();
//        return null;
//    }

    private static List<String[]> mineData(MultiValueMap<String, Object> param) throws Exception {

        List<String[]> data = new ArrayList<>();
        //fetch headers
        List<Object> heads = param.get("headers[]");
        String[] headers = new String[heads.size()];
        for (int i = 0; i < heads.size(); i++) {
            headers[i] = (String) heads.get(i);
            System.out.println("Observe Headers: " + (String) heads.get(i));
        }

        data.add(0, headers);

        //fetching data indexes
        int[] indexes = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            Object indx = param.getFirst("columns[" + i + "][data]");
            String ind = (String) indx;
            System.out.println("Observe Index: " + ind);
            if (!ind.isEmpty() && Objects.nonNull(ind)) {
                indexes[i] = Integer.parseInt((String) indx);
            } else {
                //throw new Exception("Null or empty Data Index");
            }
        }

        if (Objects.nonNull(param.get("dtrData"))) {
            //fetching the raw data
            List<Object> dtrData = param.get("dtrData");

            //mining useful data
            dtrData.stream().map(dtDatum -> {
                String[] datum = (String[]) dtDatum;
                String[] strings = new String[headers.length];
                for (int i = 0; i < strings.length; i++) {
                    strings[i] = datum[indexes[i]];

                }
                return strings;
            }).forEachOrdered(strings -> {
                data.add(strings);
            });
        } else {
            throw new Exception("No Data");
        }

        return data;

    }

    public static File

    exportToExcel(GravityDailyTotalCommission totalCommission) {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        for(int j =0; j<2; j++){
            sheet.autoSizeColumn(j);
        }

        //set the headers
//        int i = 0;
//        Row reportTitle = sheet.createRow(i);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
//        reportTitle.createCell(0).setCellValue("Commission Summary Report - "+dateFormat.format(totalCommission.getDateComputed()));
//
//        ++i;
//        sheet.createRow(i);

        Row agentCommissionTitle = sheet.createRow(0);
        agentCommissionTitle.createCell(0).setCellValue("Agents Commission");
        Row agentCommissionHeader = sheet.createRow(1);
        agentCommissionHeader.createCell(0).setCellValue("AgentDto Number");
        agentCommissionHeader.createCell(1).setCellValue("Total Commission");
        int i = 2;
        for (Map.Entry<String, BigDecimal> entry : totalCommission.getAgentTotalCommission().entrySet()) {
            Row dataRow = sheet.createRow(i);
            dataRow.createCell(0).setCellValue(entry.getKey());
            dataRow.createCell(1).setCellValue(entry.getValue().toPlainString());
            ++i;
        }

        Row bankCommissionTitle = sheet.createRow(++i);
        bankCommissionTitle.createCell(0).setCellValue("Banks Commission");
        Row bankCommissionHeader = sheet.createRow(++i);
        bankCommissionHeader.createCell(0).setCellValue("Bank Name");
        bankCommissionHeader.createCell(1).setCellValue("Total Commission");
        ++i;
        for (Map.Entry<String, BigDecimal> entry : totalCommission.getAgentBankTotalCommission().entrySet()) {
            Row dataRow = sheet.createRow(i);
            dataRow.createCell(0).setCellValue(entry.getKey());
            dataRow.createCell(1).setCellValue(entry.getValue().toPlainString());
            ++i;
        }


        Row _3lineCommissionHeader = sheet.createRow(++i);
        _3lineCommissionHeader.createCell(0).setCellValue("Agency Organization");
        _3lineCommissionHeader.createCell(1).setCellValue("Total Commission");
        ++i;
        for (Map.Entry<String, BigDecimal> entry : totalCommission.get_3lineTotalCommission().entrySet()) {
            Row dataRow = sheet.createRow(i);
            dataRow.createCell(0).setCellValue(entry.getKey());
            dataRow.createCell(1).setCellValue(entry.getValue().toPlainString());
            ++i;
        }

        for(int j =0; j<2; j++){
            sheet.autoSizeColumn(j);
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(totalCommission.getDateComputed());

        File reportDirectory = new File("C:\\GravityReports\\");



        File file = new File(reportDirectory.getAbsolutePath()+"\\CommissionReport_"+date+".xlsx");
        if(!reportDirectory.exists()){
            try {
                reportDirectory.mkdir();
                file.createNewFile();
            } catch (IOException e) {
                Logger.getLogger(ExportUtility.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        try {

            FileOutputStream fileOut;
            fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExportUtility.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExportUtility.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            Logger.getLogger(ExportUtility.class.getName()).log(Level.SEVERE, null, e);
        }

        return file;
    }

    public static File exportToExcel(MultiValueMap<String, Object> param) throws Exception {

        //fetch fileName
        String fileName = (String) param.get("fileName").get(0);
        System.out.println("Filename: " + fileName);

        //fetch data
        List<String[]> data = mineData(param);

        //fetch headers from data
        String[] headers = data.get(0);

        //if no value records i.e only header
        if (data.size() < 2) {
            return null;
        }

        //checking for matching column lengths
        if (headers.length != data.get(1).length) {

            System.out.println("Table would be error prone");

            throw new Exception("Length of headers is not equal to length of one row of data");

        }

        //Creating temporary file
        Path path = null;

        try {
            String name = fileName.replace(" ", "_").replace("/", "_");
            System.out.println(name);
            path = Files.createTempFile(name, ".xlsx");
        } catch (IOException ex) {
            Logger.getLogger(ExportUtility.class.getName()).log(Level.SEVERE, null, ex);
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        //set the headers
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        //Populate the data
        int i = 1;
        for (String[] datum : data) {
            if (data.indexOf(datum) != 0) {
                Row row = sheet.createRow(i);
                for (int j = 0; j < datum.length; j++) {
                    row.createCell(j).setCellValue(datum[j]);

                }
                i++;
            }
        }
        File file = path.toFile();
        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExportUtility.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExportUtility.class.getName()).log(Level.SEVERE, null, ex);
        }

        return file;

    }

    public static File exportToPDF(MultiValueMap<String, Object> param) throws Exception {

        String fileName = (String) param.get("fileName").get(0);

        List<String[]> data = mineData(param);

        String[] headers = data.get(0);
        //if no value records i.e only header
        if (data.size() < 2) {
            return null;
        }
        //checking for matching column lengths
        if (headers.length != data.get(1).length) {

            System.out.println("Table would be error prone");

            throw new Exception("Length of headers is not equal to length of one row of data");

        }

        Path path = null;

        try {
            path = Files.createTempFile(fileName, ".pdf");
        } catch (IOException ex) {
            Logger.getLogger(ExportUtility.class.getName()).log(Level.SEVERE, null, ex);
        }

        PdfWriter pdfWriter = null;
        try {
            pdfWriter = new PdfWriter(path.toFile());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExportUtility.class.getName()).log(Level.SEVERE, null, ex);
        }

        PdfDocument pdfDocument = new PdfDocument(pdfWriter);

        Document document = new Document(pdfDocument, PageSize.A4.rotate());

        document.setMargins(20, 20, 20, 20);

        //Table table = new Table(new float[]{5, 5, 5, 5, 5, 5, 5, 4, 5, 5});
        Table table = new Table(new float[headers.length]);

        table.setWidthPercent(100);

        //set headers
        for (int i = 0; i < headers.length; i++) {
            table.addHeaderCell(new Cell().add(new Paragraph(headers[i]).setBold()));
        }

        //Populate the data
        for (String[] datum : data) {
            if (data.indexOf(datum) != 0) {

                for (int j = 0; j < datum.length; j++) {
                    table.addCell(new Cell().add(new Paragraph(datum[j])));
                    System.out.println("ADDING DATA : " + datum[j]);
                }
            }

        }

        document.add(table);
        document.close();

        return path.toFile();
    }

    public static File exportToCSV(MultiValueMap<String, Object> param) throws Exception {

        String fileName = (String) param.get("fileName").get(0);

        List<String[]> data = mineData(param);

        String[] headers = data.get(0);
        //if no value records i.e only header
        if (data.size() < 2) {
            return null;
        }
        //checking for matching column lengths
        if (headers.length != data.get(1).length) {

            System.out.println("Table would be error prone");

            throw new Exception("Length of headers is not equal to length of one row of data");

        }

        Path path = null;

        try {
            path = Files.createTempFile(fileName, ".csv");
        } catch (IOException ex) {
            Logger.getLogger(ExportUtility.class.getName()).log(Level.SEVERE, null, ex);
        }

        CSVWriter cSVWriter = null;
        try {
            cSVWriter = new CSVWriter(new FileWriter(path.toFile()), ',', '\"', '\\', "\n");
        } catch (IOException ex) {
            Logger.getLogger(ExportUtility.class.getName()).log(Level.SEVERE, null, ex);
        }

        cSVWriter.writeAll(data, true);

        try {
            cSVWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(ExportUtility.class.getName()).log(Level.SEVERE, null, ex);
        }

        return path.toFile();
    }


    private static String prepareThisDataForPage(String currentPage, int index, String data) {
        switch (Pages.getPageConstant(currentPage)) {
            case Pages.DEFAULT:
                return data;
            case Pages.PAGE_TRANSACTIONS:
                return Pages.prepTransaction(index, data);
            case Pages.PAGE_LIQUIDATIONS:
                return Pages.prepLiquidation(index, data);

            default:
                return data;
        }

    }


}
