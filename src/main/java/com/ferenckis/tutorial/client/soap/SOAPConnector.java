package com.ferenckis.tutorial.client.soap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

@Component
public class SOAPConnector {

    private static final Logger LOG = LoggerFactory.getLogger(SOAPConnector.class);

    @Autowired
    private SOAPConnectionFactory soapConnectionFactory;

    public SOAPMessage call(String url, SOAPMessage soapMessage) {
        SOAPConnection soapConnection = null;
        try {
            soapConnection = soapConnectionFactory.createConnection();
            return soapConnection.call(soapMessage, url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (soapConnection != null) {
                try {
                    soapConnection.close();
                } catch (SOAPException e) {
                    LOG.warn("Closing soapConnection failed - {}", e);
                }
            }
        }
    }
}
