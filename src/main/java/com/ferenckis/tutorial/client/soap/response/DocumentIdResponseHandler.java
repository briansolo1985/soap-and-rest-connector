package com.ferenckis.tutorial.client.soap.response;

import com.ferenckis.tutorial.client.soap.SoapClientFacade.Service;
import com.ferenckis.tutorial.util.NameSpaceContextHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.NodeList;

import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPath;

import static com.ferenckis.tutorial.client.soap.SoapClientFacade.Service.DOCUMENT_ID;
import static com.google.common.base.Preconditions.checkState;
import static javax.xml.xpath.XPathConstants.NODESET;

@Component
public class DocumentIdResponseHandler extends AbstractResponseHandler {

    @Value("${soap.document.id.service.response.xpath}")
    private String workdaySoapDocumentIdServiceResponseXpath;

    @Override
    protected void addCustomNameSpaces(NameSpaceContextHelper contextHelper) {
        contextHelper.declareNamespace("wd", "urn:com.wd/bsvc");
    }

    @Override
    protected String parseResponse(SOAPMessage message, XPath xPath) {
        try {
            NodeList nodeList = (NodeList) xPath.evaluate(workdaySoapDocumentIdServiceResponseXpath, message.getSOAPBody(), NODESET);
            checkState(nodeList.getLength() == 1, "SOAPBody must contain only 1 documentId");
            return nodeList.item(0).getNodeValue();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isFor(Service service) {
        return DOCUMENT_ID.equals(service);
    }
}
