package com.ferenckis.tutorial.client.soap.request;

import com.ferenckis.tutorial.client.soap.SoapClientFacade.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;

import static com.ferenckis.tutorial.client.soap.SoapClientFacade.Service.DOCUMENT_ID;
import static com.google.common.base.Preconditions.checkArgument;

@Component
public class DocumentIdRequestFactory extends AbstractRequestFactory {

    public static final String INTEGRATION_ID_KEY = "integration.id";

    @Value("${soap.document.id.service.address}")
    private String workdaySoapDocumentIdServiceAddress;

    @Override
    protected void addCustomNameSpaces(SOAPEnvelope soapEnvelope) throws SOAPException {
        soapEnvelope.addNamespaceDeclaration("wd", "urn:com.wd/bsvc");
    }

    @Override
    protected void addBody(SOAPEnvelope soapEnvelope) throws SOAPException {
        SOAPBody body = soapEnvelope.getBody();

        SOAPElement getEventDocumentsRequest = body.addChildElement("Get_Event_Documents_Request", "wd");

        SOAPElement requestReferences = getEventDocumentsRequest.addChildElement("Request_References", "wd");
        SOAPElement integrationEventReference = requestReferences.addChildElement("Integration_Event_Reference", "wd");
        SOAPElement id = integrationEventReference.addChildElement("ID", "wd");
        id.setAttribute("wd:type", "WID");
        id.setValue(getIntegrationId());

        SOAPElement responseFilter = getEventDocumentsRequest.addChildElement("Response_Filter", "wd");
        SOAPElement page = responseFilter.addChildElement("Page", "wd");
        page.setValue("1");
        SOAPElement count = responseFilter.addChildElement("Count", "wd");
        count.setValue("1");

    }

    private String getIntegrationId() {
        Object integrationId = parameters.get(INTEGRATION_ID_KEY);
        checkArgument(integrationId != null, "Integration id must be provided");
        checkArgument(integrationId instanceof String, "Integration id must be a string");
        return (String) integrationId;
    }

    @Override
    public boolean isFor(Service service) {
        return DOCUMENT_ID.equals(service);
    }

    @Override
    public String getServiceAddress() {
        return workdaySoapDocumentIdServiceAddress;
    }
}
