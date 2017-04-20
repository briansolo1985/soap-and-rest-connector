package com.ferenckis.tutorial.client.soap.response;

import com.ferenckis.tutorial.util.NameSpaceContextHelper;
import org.springframework.stereotype.Component;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

@Component
public abstract class AbstractResponseHandler implements ResponseHandler {

    public final String getResult(SOAPMessage response) {
        checkAndThrowIfError(response);
        return parseResponse(response, createXPath());
    }

    private void checkAndThrowIfError(SOAPMessage message) {
        try {
            SOAPBody body = message.getSOAPBody();
            if(body.hasFault()) {
                SOAPFault fault = body.getFault();
                throw new RuntimeException(fault.getFaultCode() + " - " + fault.getFaultString());
            }
        } catch (SOAPException e) {
            throw new RuntimeException(e);
        }
    }

    private XPath createXPath() {
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        xpath.setNamespaceContext(createContextHelper());
        return xpath;
    }

    private NameSpaceContextHelper createContextHelper() {
        NameSpaceContextHelper contextHelper = new NameSpaceContextHelper();
        contextHelper.declareNamespace("env", "http://schemas.xmlsoap.org/soap/envelope/");
        addCustomNameSpaces(contextHelper);
        return contextHelper;
    }

    protected abstract void addCustomNameSpaces(NameSpaceContextHelper contextHelper);

    protected abstract String parseResponse(SOAPMessage message, XPath xPath);
}
