package com._3line.gravity.freedom.utility;


import com._3line.gravity.core.exceptions.GravityException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.FormatterClosedException;

public class ExportCsv {

    //create temp folder if not exists
    public static boolean prepareDirectory = new File(Constants.apptempfolder).exists() ? true : new File(Constants.apptempfolder).mkdir();

    Formatter outputfile = null;

    public void createCSV(ArrayList<String[]> rs) throws GravityException {
        createCSV(rs, null);
    }

    public void createCSV(ArrayList<String[]> rs, String Name) throws GravityException {
        Name = Name == null ? "default" : Name;
        Formatter outputfile = null;
        try {
            outputfile = new Formatter(Constants.apptempfolder + Name);
        } catch (SecurityException se) {
            System.out.println("caught security exception");
            throw new GravityException("You don't have permission to creeate file");

        } catch (FileNotFoundException fn) {
            System.out.println("caught foundfilenot exception");
            throw new GravityException("FIle was not found");
        }

        try {

            for (int i = 0; i < rs.size(); i++) {

                for (int j = 0; j < rs.get(i).length; j++) {
                    outputfile.format(getProperCSVField(rs.get(i)[j]) + ",");
                }

                outputfile.format("\r\n");
            }

        } catch (FormatterClosedException fc) {
            System.out.println("caught formterr closed exception");
            throw new GravityException("Writter ink is dried");
        } finally {
            outputfile.close();
        }

        if (outputfile != null) {
            outputfile.close();
        }
    }

    public void createCSV() throws GravityException {
        String N = null;
        createCSV(N);
    }

    public void createCSV(String Name) throws GravityException {
        Name = Name == null ? "default" : Name;

        try {
            outputfile = new Formatter(Constants.apptempfolder + Name);
        } catch (SecurityException se) {
            System.out.println("caught security exception");
            throw new GravityException("You don't have permission to creeate file");

        } catch (FileNotFoundException fn) {

            System.out.println("caught foundfilenot exception");
            fn.printStackTrace();
        }

    }

    public void write(Object... rs) throws GravityException {

        try {

            for (int j = 0; j < rs.length; j++) {
                outputfile.format(getProperCSVField((String) rs[j]) + ",");
            }

            outputfile.format("\r\n");

        } catch (FormatterClosedException fc) {
            System.out.println("caught formterr closed exception");
            throw new GravityException("Writter ink is dried");
        }

    }

    public void close() throws GravityException {

        if (outputfile != null) {
            outputfile.close();
        }
    }

    public static void main(String[] as) {
        ArrayList<String[]> rs = new ArrayList<String[]>();
        String[] t1 = {"DATE", "YEAR", "HEIGHT"};
        String[] t2 = {"12-02-2014", "2134", "45"};
        rs.add(t1);
        rs.add(t2);
        try {
            ExportCsv csv = new ExportCsv();
            csv.createCSV("test");
            csv.write((Object[]) t1);
            csv.write((Object[]) t2);
            csv.close();
            System.out.println(System.getProperty("user.dir"));
        } catch (GravityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String getProperCSVField(String field) {
        if (field == null || field.equals("")) {
            return "";
        }

        if (field.contains("\"")) {
            field = field.replaceAll("\"", "\"\"");
        }

        if (field.contains(",")) {
            field = "\"" + field + "\"";
        }
        return field;
    }
}
