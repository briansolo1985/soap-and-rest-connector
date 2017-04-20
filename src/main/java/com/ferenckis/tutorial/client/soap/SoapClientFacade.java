package com.ferenckis.tutorial.client.soap;

import com.ferenckis.tutorial.client.soap.request.RequestFactory;
import com.ferenckis.tutorial.client.soap.request.RequestFactoryLocator;
import com.ferenckis.tutorial.client.soap.response.ResponseHandlerLocator;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.soap.SOAPMessage;
import java.util.Map;

import static com.ferenckis.tutorial.client.soap.SoapClientFacade.Service.DOCUMENT_ID;
import static com.ferenckis.tutorial.client.soap.SoapClientFacade.Service.INTEGRATION_ID;
import static com.ferenckis.tutorial.client.soap.request.DocumentIdRequestFactory.INTEGRATION_ID_KEY;
import static com.ferenckis.tutorial.util.SOAPUtils.print;

@Component
public class SoapClientFacade {

    private static final Logger LOG = LoggerFactory.getLogger(SoapClientFacade.class);

    @Autowired
    private SOAPConnector soapConnector;

    @Autowired
    private RequestFactoryLocator requestFactoryLocator;

    @Autowired
    private ResponseHandlerLocator responseHandlerLocator;

    public String callIntegrationIdService() {
        return callService(INTEGRATION_ID, ImmutableMap.of());
    }

    public String callDocumentIdService(String integrationId) {
        return callService(DOCUMENT_ID, ImmutableMap.of(INTEGRATION_ID_KEY, integrationId));
    }

    private String callService(Service service, Map<String, Object> parameters) {
        String serviceAddress = null;
        SOAPMessage request = null;
        SOAPMessage response = null;
        try {
            RequestFactory requestFactory = requestFactoryLocator.getFactory(service);
            request = requestFactory.newMessage(parameters);
            serviceAddress = requestFactory.getServiceAddress();
            response = soapConnector.call(serviceAddress, request);
            return responseHandlerLocator.getResponseHandler(service).getResult(response);
        } catch (Exception e) {
            LOG.info("Service address {}", serviceAddress);
            LOG.info("Request message");
            print(request);
            LOG.info("Response message");
            print(response);
            throw new RuntimeException(e);
        }
    }

    public enum Service {
        INTEGRATION_ID,
        DOCUMENT_ID
    }
}
