package com.ferenckis.tutorial.util;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.soap.SOAPMessage;

public class SOAPUtils {

    private static final Logger LOG = LoggerFactory.getLogger(SOAPUtils.class);

    public static void print(SOAPMessage soapMessage) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            soapMessage.writeTo(byteArrayOutputStream);
            LOG.info(byteArrayOutputStream.toString());
        } catch (Exception e) {
            LOG.warn("Error logging SOAP message - {}", e);
        }
    }
}
