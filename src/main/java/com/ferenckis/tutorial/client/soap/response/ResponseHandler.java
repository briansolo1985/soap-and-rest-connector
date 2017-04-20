package com.ferenckis.tutorial.client.soap.response;

import com.ferenckis.tutorial.client.soap.SoapClientFacade.Service;

import javax.xml.soap.SOAPMessage;

public interface ResponseHandler {

    boolean isFor(Service service);

    String getResult(SOAPMessage response);
}
