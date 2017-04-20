package com.ferenckis.tutorial.client.soap.response;

import com.ferenckis.tutorial.client.soap.SoapClientFacade.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResponseHandlerLocator {

    @Autowired
    private ResponseHandler[] responseHandlers;

    public ResponseHandler getResponseHandler(Service service) {
        for (ResponseHandler responseHandler : responseHandlers) {
            if (responseHandler.isFor(service)) {
                return responseHandler;
            }
        }
        throw new RuntimeException("ResponseHandler was not found for type " + service);
    }
}
