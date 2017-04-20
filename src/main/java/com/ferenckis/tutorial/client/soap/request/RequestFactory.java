package com.ferenckis.tutorial.client.soap.request;

import com.ferenckis.tutorial.client.soap.SoapClientFacade.Service;

import javax.xml.soap.SOAPMessage;
import java.util.Map;

public interface RequestFactory {

    boolean isFor(Service service);

    String getServiceAddress();

    SOAPMessage newMessage();

    SOAPMessage newMessage(Map<String, Object> parameters);
}
