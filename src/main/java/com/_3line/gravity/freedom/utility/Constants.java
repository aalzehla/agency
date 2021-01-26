package com._3line.gravity.freedom.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.function.Function;

public class Constants {

    public static long OtpExpiryMin = 5;

    public static int CacheReset = 5;
    public static int BearerExpiryMin = 60;
    public static long MaximumParallelUser = 20000;

    public static String apptempfolder = getTempFolder("temp");


    public static String getTempFolder(String name) {
        try {
            String parent = new File(Constants.class.getClassLoader().getResource("message.properties").toString()).getParent().replace("file:/", "").replace("file:\\", "");
            //for linux hack prepend pathseparator befor the path to make absolute path
            parent = File.separator + parent;
            String path = parent + File.separator + name;
            File dir = new File(path);
            dir.mkdirs();
            return path + File.separator;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
