/*
 * @(#)EHFNamespacePrefixMapper.java
 *
 * Copyright (c) 2012, Zirius AS.
 * All rights reserved. 
 * 
 * Use is subject to license terms. This software is protected by 
 * copyright law and international treaties. Unauthorized reproduction or 
 * distribution of this program, or any portion of it, may result in severe 
 * civil and criminal penalties, and will be prosecuted to the maximum extent.
 */
package ehandel.no.util;

//import com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper; // For ERP
import com.sun.xml.bind.marshaller.NamespacePrefixMapper; // For ZERP

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The Class EHFNamespacePrefixMapper.
 * 
 * @author amuthar
 * @since ehf; Feb 29, 2012
 */
public class EHFInternalNamespacePrefixMapper extends NamespacePrefixMapper {

    private static EHFInternalNamespacePrefixMapper instance;
    private Map<String, String> uriPrefixMap = null;
    private List<String> defaultNSUri = null;

    private EHFInternalNamespacePrefixMapper() {
        init();
    }

    public static synchronized EHFInternalNamespacePrefixMapper getInstance() {
        if (instance == null) {
            instance = new EHFInternalNamespacePrefixMapper();
        }
        return instance;
    }

    private void init() {

        uriPrefixMap = new HashMap<String, String>(6);
        uriPrefixMap.put(
                "urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2", "cac");
        uriPrefixMap.put("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2",
                "cbc");
        uriPrefixMap.put(
                "urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2", "ext");
        uriPrefixMap.put("urn:oasis:names:specification:ubl:schema:xsd:QualifiedDatatypes-2", "qdt");
        uriPrefixMap.put(
                "urn:un:unece:uncefact:data:specification:UnqualifiedDataTypesSchemaModule:2",
                "udt");
        uriPrefixMap.put("urn:un:unece:uncefact:documentation:2", "ccts");

        defaultNSUri = new ArrayList<String>(1);
        defaultNSUri.add("urn:oasis:names:specification:ubl:schema:xsd:Invoice-2");
    }

    @Override
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {

        if (uriPrefixMap.containsKey(namespaceUri)) {
            return uriPrefixMap.get(namespaceUri);
        }

        if (defaultNSUri.contains(namespaceUri)) {
            return "";
        }

        return suggestion;
    }

}
