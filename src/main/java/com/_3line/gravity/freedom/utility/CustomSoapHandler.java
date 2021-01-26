/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.utility;

/**
 *
 * @author OlalekanW
 */


import javax.xml.namespace.QName;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayOutputStream;
import java.util.Set;

public class CustomSoapHandler implements SOAPHandler<SOAPMessageContext> {
 
 private String request = "",response ="";
 
 public boolean handleMessage(SOAPMessageContext soapMessageContext) {
 
 Boolean outboundProperty = (Boolean) soapMessageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
 SOAPMessage currentMessage= soapMessageContext.getMessage();
 boolean isOutboundMessage=  (Boolean)soapMessageContext.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY);
 

 
 try {
 SOAPEnvelope soapEnvelope = currentMessage.getSOAPPart().getEnvelope();

 //FOR LOGGING
            ByteArrayOutputStream baos = new ByteArrayOutputStream();   
            try {
                currentMessage.writeTo(baos);                    
		if(isOutboundMessage){ this.request = baos.toString("UTF-8"); }
                else{
                    System.out.println(">>>>>>>>>>>>>XXXXXXX<<<<<<<<SETTING RESPONSE");
                    this.response = baos.toString("UTF-8");System.out.println(response);
                    }
	        } catch (Exception e) {e.printStackTrace();}
 
     } catch (Exception e) {
           throw new RuntimeException("Error on wsSecurityHandler: " + e.getMessage());
                       }
 

 
 return true;
 }
 
 
 //response at fault
 @Override
 public boolean handleFault(SOAPMessageContext context) {
 //FOR LOGGING
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                SOAPMessage currentMessage = context.getMessage();
                boolean isOutboundMessage=  (Boolean)context.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY);
                currentMessage.writeTo(baos);                    
		if(!isOutboundMessage){
                    System.out.println(">>>>>>>>>>>>>XXXXXXX<<<<<<<<SETTING RESPONSE");
                    this.response = baos.toString("UTF-8");System.out.println(response);
                                      }
	        } catch (Exception e) {e.printStackTrace();}     
 return true;
                                                       }
 
 
 @Override
 public void close(MessageContext context) {
 // TODO Auto-generated method stub
 }
 

 
 @Override
 public Set<QName> getHeaders() {
 // TODO Auto-generated method stub
 return null;
 }

    /**
     * @return the request
     */
    public String getRequest() {
        return request;
                              }

    /**
     * @param request the request to set
     */
    public void setRequest(String request) {
        this.request = request;
                                         }

    /**
     * @return the response
     */
    public String getResponse() {
        return response;
    }

    /**
     * @param response the response to set
     */
    public void setResponse(String response) {
        this.response = response;
    }
}