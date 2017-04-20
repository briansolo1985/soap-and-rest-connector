package com.ferenckis.tutorial.util;

import javax.xml.namespace.NamespaceContext;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NameSpaceContextHelper implements NamespaceContext {
    private final Map<String, String> prefixedNameSpaces = new HashMap<>();

    public void declareNamespace(String prefix, String nameSpace) {
        prefixedNameSpaces.put(prefix, nameSpace);
    }

    @Override
    public String getNamespaceURI(String prefix) {
        return prefixedNameSpaces.get(prefix);
    }

    @Override
    public String getPrefix(String namespaceURI) {
        for(String nameSpace : prefixedNameSpaces.values()) {
            if(nameSpace.equals(namespaceURI)) {
                return nameSpace;
            }
        }
        return null;
    }

    @Override
    public Iterator getPrefixes(String namespaceURI) {
        return prefixedNameSpaces.keySet().iterator();
    }
}
