package com.ferenckis.tutorial.client.soap.response;

import com.ferenckis.tutorial.client.soap.SoapClientFacade.Service;
import com.ferenckis.tutorial.util.NameSpaceContextHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.soap.Node;
import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPath;

import static com.ferenckis.tutorial.client.soap.SoapClientFacade.Service.INTEGRATION_ID;
import static javax.xml.xpath.XPathConstants.NODE;

@Component
public class IntegrationIdResponseHandler extends AbstractResponseHandler {

    @Value("${soap.integration.id.service.response.xpath}")
    private String workdaySoapIntegrationIdServiceResponseXpath;

    @Override
    protected void addCustomNameSpaces(NameSpaceContextHelper contextHelper) {
        contextHelper.declareNamespace("wd", "urn:com.report/Hadoop_Master_Integration_Event");
    }

    @Override
    protected String parseResponse(SOAPMessage message, XPath xPath) {
        try {
            return ((Node) xPath.evaluate(workdaySoapIntegrationIdServiceResponseXpath, message.getSOAPBody(), NODE)).getValue();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isFor(Service service) {
        return INTEGRATION_ID.equals(service);
    }
}
