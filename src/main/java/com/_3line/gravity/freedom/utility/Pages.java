/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.utility;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author OlalekanW
 */
public class Pages {
    private static Map<String,Integer> pages;
    private static PropertyResource pr =  new PropertyResource();
        
    public static final int DEFAULT = -1;
    public static final int PAGE_TRANSACTIONS = 0;
    public static final int PAGE_LIQUIDATIONS = 1;
    
    static{
        pages = new HashMap<String,Integer>();
        pages.put("",DEFAULT);
        pages.put(pr.getV("path.transactions","angular.properties"),PAGE_TRANSACTIONS);
        pages.put(pr.getV("path.liquidations","angular.properties"),PAGE_LIQUIDATIONS);
          }
    
   public static Integer getPageConstant(String currentPage){ Integer page = pages.get(currentPage); return page==null ? DEFAULT : page; }
           
   
    public static String prepTransaction(int index, String data){  
        if(index==6){return getStatus(data);}        
        return data;
                                                                }
    
    public static String prepLiquidation(int index, String data){  
        if(index==6 || index==7 || index==8 || index==9 || index==10){return formatKoboToNaira2dp(data);}  
        if(index==11){return getStatus(data);}         
        return data;
                                                                }   
    
 
    
    
    
    
    private static String formatKoboToNaira2dp(String data){ return String.format("%.2f" , (Double.parseDouble(data)/100));}    
    private static String getStatus(String data){         
            if(data.trim().equals("1")){return "SUCCESSFUL";}
            if(data.trim().equals("0")){return "FAILED";}
            if(data.trim().equals("2")){return "PENDING";}
            if(data.trim().equals("3")){return "REJECTED";}
            return "";
                                                }
}
