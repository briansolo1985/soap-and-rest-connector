package com.ferenckis.tutorial.client.soap.request;

import com.ferenckis.tutorial.client.soap.SoapClientFacade.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;

import static com.ferenckis.tutorial.client.soap.SoapClientFacade.Service.INTEGRATION_ID;

@Component
public class IntegrationIdRequestFactory extends AbstractRequestFactory {

    @Value("${soap.integration.id.service.address}")
    private String workdaySoapIntegrationIdServiceAddress;

    @Override
    protected void addCustomNameSpaces(SOAPEnvelope soapEnvelope) throws SOAPException {
        soapEnvelope.addNamespaceDeclaration("had", "urn:com.report/Hadoop_Master_Integration_Event");
    }

    @Override
    protected void addBody(SOAPEnvelope soapEnvelope) throws SOAPException {
        SOAPBody body = soapEnvelope.getBody();
        body.addChildElement("Execute_Report", "had");

//        THIS CODE SEEMS TO BE UNNECESSARY AS AUTHENTICATION IS IN HEADER
//        SOAPElement authentication = executeReport.addChildElement("Authentication", "had");
//        SOAPElement proxyUserName = authentication.addChildElement("Proxy_User_Name", "had");
//        proxyUserName.setAttribute("xsi:type", "xsd:string");
//        proxyUserName.setValue("Hadoop@aviva10");
    }

    @Override
    public boolean isFor(Service service) {
        return INTEGRATION_ID.equals(service);
    }

    @Override
    public String getServiceAddress() {
        return workdaySoapIntegrationIdServiceAddress;
    }
}
