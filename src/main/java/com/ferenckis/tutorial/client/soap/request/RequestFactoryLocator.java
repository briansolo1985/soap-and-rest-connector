package com.ferenckis.tutorial.client.soap.request;

import com.ferenckis.tutorial.client.soap.SoapClientFacade.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RequestFactoryLocator {

    @Autowired
    private RequestFactory[] requestFactories;

    public RequestFactory getFactory(Service service) {
        for (RequestFactory requestFactory : requestFactories) {
            if (requestFactory.isFor(service)) {
                return requestFactory;
            }
        }
        throw new RuntimeException("RequestFactory was not found for type " + service);
    }
}
