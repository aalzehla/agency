/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.utility;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;

/**
 *
 * @author OlalekanW
 */

@XmlRootElement(name = "SmsRequest")
@XmlType(propOrder = {"messageId", "title", "message", "mobileNumbers" })
public class SMSRequest<T> {
    private String messageId;
    private String title;
    private String message;
    private ArrayList<String> mobileNumbers;

    public SMSRequest(){}






    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the mobileNumbers
     */
    
    @XmlElementWrapper(name = "MobileNumbers") //uSED TO WRAP COLLECTIONs
    public ArrayList<String> getMobileNumbers() {
        return mobileNumbers;
    }

    /**
     * @param mobileNumbers the mobileNumbers to set
     */
    public void setMobileNumbers(ArrayList<String> mobileNumbers) {
        this.mobileNumbers = mobileNumbers;
    }

    /**
     * @return the messageId
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * @param messageId the messageId to set
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }


    public String toString(){ 
    String xml = "";
    try{
    JAXBContext contextObj = JAXBContext.newInstance(getClass());    
    Marshaller marshallerObj = contextObj.createMarshaller();  
    marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); 
    
    java.io.StringWriter sw = new java.io.StringWriter();

    marshallerObj .setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
    marshallerObj .marshal(this, sw);
    xml = sw.toString();
    
        }catch(JAXBException e){e.printStackTrace();}
    return xml;
    }
}
