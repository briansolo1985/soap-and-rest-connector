package com.ferenckis.tutorial.client.soap.request;

import com.ferenckis.tutorial.client.credential.Credentials;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.soap.*;
import java.util.HashMap;
import java.util.Map;

@Component
public abstract class AbstractRequestFactory implements com.ferenckis.tutorial.client.soap.request.RequestFactory {

    protected Map<String, Object> parameters = new HashMap<>();
    @Autowired
    private javax.xml.soap.MessageFactory messageFactory;
    @Autowired
    private Credentials workdayCredentials;

    public final SOAPMessage newMessage() {
        return newMessage(ImmutableMap.of());
    }

    public final SOAPMessage newMessage(Map<String, Object> parameters) {
        this.parameters.clear();
        this.parameters.putAll(parameters);

        try {
            SOAPMessage soapMessage = messageFactory.createMessage();
            SOAPEnvelope envelope = createEnvelope(soapMessage);

            addDefaultNameSpaces(envelope);
            addCustomNameSpaces(envelope);
            addSecurityHeader(envelope);
            addBody(envelope);

            soapMessage.saveChanges();
            return soapMessage;
        } catch (SOAPException e) {
            throw new RuntimeException(e);
        }
    }

    private SOAPEnvelope createEnvelope(SOAPMessage soapMessage) throws SOAPException {
        SOAPEnvelope soapEnvelope = soapMessage.getSOAPPart().getEnvelope();
        soapEnvelope.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding/");
        return soapEnvelope;
    }

    private void addDefaultNameSpaces(SOAPEnvelope soapEnvelope) throws SOAPException {
        soapEnvelope.addNamespaceDeclaration("wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
        soapEnvelope.addNamespaceDeclaration("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
        soapEnvelope.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
    }

    protected abstract void addCustomNameSpaces(SOAPEnvelope soapEnvelope) throws SOAPException;

    private void addSecurityHeader(SOAPEnvelope soapEnvelope) throws SOAPException {
        SOAPHeader header = soapEnvelope.getHeader();

        SOAPElement securityTag = header.addChildElement("Security", "wsse");
        securityTag.setAttribute("soapenv:mustUnderstand", "1");
        SOAPElement userNameToken = securityTag.addChildElement("UsernameToken", "wsse");

        SOAPElement userName = userNameToken.addChildElement("Username", "wsse");
        userName.setAttribute("xsi:type", "xsd:string");
        userName.setValue(workdayCredentials.getWorkdayUserName());

        SOAPElement password = userNameToken.addChildElement("Password", "wsse");
        password.setAttribute("Type", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
        password.setAttribute("xsi:type", "xsd:string");
        password.setValue(workdayCredentials.getWorkdayPassword());
    }

    protected abstract void addBody(SOAPEnvelope soapEnvelope) throws SOAPException;
}
