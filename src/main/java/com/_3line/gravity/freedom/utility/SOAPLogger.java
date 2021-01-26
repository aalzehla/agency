package com._3line.gravity.freedom.utility;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import java.util.ArrayList;
import java.util.List;

public class SOAPLogger {

    public static void dumpRequestResponse(BindingProvider service,CustomSoapHandler customHandler){
        //sets handler for request and response details
        BindingProvider bindingProvider =  service ;
        @SuppressWarnings("rawtypes")
        List<Handler> handlerChain = new ArrayList<Handler>();
        handlerChain.add(customHandler);
        bindingProvider.getBinding().setHandlerChain(handlerChain);
    }
}
