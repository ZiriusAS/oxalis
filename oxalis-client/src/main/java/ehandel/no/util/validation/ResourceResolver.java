/*
 * @(#)ResourceResolver.java
 *
 * Copyright (c) 2012, Zirius AS.
 * All rights reserved. 
 * 
 * Use is subject to license terms. This software is protected by 
 * copyright law and international treaties. Unauthorized reproduction or 
 * distribution of this program, or any portion of it, may result in severe 
 * civil and criminal penalties, and will be prosecuted to the maximum extent.
 */

package ehandel.no.util.validation;

import java.io.InputStream;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

/**
 * Resolve common schemas referred in invoice and creditnote.
 * 
 * @author senthilkumarn
 * @since ehf; Mar 16, 2012
 */
public class ResourceResolver implements LSResourceResolver {

    private LSResourceResolver parent = null;
    private Set<String> returnedResources = new HashSet<String>();

    public ResourceResolver() {
    }

    public ResourceResolver(LSResourceResolver r) {
        parent = r;
    }

    public LSInput resolveResource(String type, String namespaceURI, String publicId,
            String systemId, String baseURI) {

        LSInput input = new LSInputImpl();

        if (returnedResources.contains(systemId)) {
            return null;
        } else if (systemId.contains("UBL-CommonAggregateComponents-2.0.xsd")) {
            input.setByteStream(this.getClass().getResourceAsStream(
                    "/ehandel/common/UBL-CommonAggregateComponents-2.0.xsd"));
        } else if (systemId.contains("UBL-CommonBasicComponents-2.0.xsd")) {
            input.setByteStream(this.getClass().getResourceAsStream(
                    "/ehandel/common/UBL-CommonBasicComponents-2.0.xsd"));
        } else if (systemId.contains("UBL-CommonExtensionComponents-2.0.xsd")) {
            input.setByteStream(this.getClass().getResourceAsStream(
                    "/ehandel/common/UBL-CommonExtensionComponents-2.0.xsd"));
        } else if (systemId.contains("UBL-ExtensionContentDatatype-2.0.xsd")) {
            input.setByteStream(this.getClass().getResourceAsStream(
                    "/ehandel/common/UBL-ExtensionContentDatatype-2.0.xsd"));
        } else if (systemId.contains("UBL-QualifiedDatatypes-2.0.xsd")) {
            input.setByteStream(this.getClass().getResourceAsStream(
                    "/ehandel/common/UBL-QualifiedDatatypes-2.0.xsd"));
        } else if (systemId.contains("UnqualifiedDataTypeSchemaModule-2.0.xsd")) {
            input.setByteStream(this.getClass().getResourceAsStream(
                    "/ehandel/common/UnqualifiedDataTypeSchemaModule-2.0.xsd"));
        } else if (systemId.contains("CodeList_CurrencyCode_ISO_7_04.xsd")) {
            input.setByteStream(this.getClass().getResourceAsStream(
                    "/ehandel/common/CodeList_CurrencyCode_ISO_7_04.xsd"));
        } else if (systemId.contains("CodeList_LanguageCode_ISO_7_04.xsd")) {
            input.setByteStream(this.getClass().getResourceAsStream(
                    "/ehandel/common/CodeList_LanguageCode_ISO_7_04.xsd"));
        } else if (systemId.contains("CodeList_MIMEMediaTypeCode_IANA_7_04.xsd")) {
            input.setByteStream(this.getClass().getResourceAsStream(
                    "/ehandel/common/CodeList_MIMEMediaTypeCode_IANA_7_04.xsd"));
        } else if (systemId.contains("CodeList_UnitCode_UNECE_7_04.xsd")) {
            input.setByteStream(this.getClass().getResourceAsStream(
                    "/ehandel/common/CodeList_UnitCode_UNECE_7_04.xsd"));
        } else if (systemId.contains("CCTS_CCT_SchemaModule-2.1.xsd")) {
            input.setByteStream(this.getClass().getResourceAsStream(
                    "/ehandel/common/CCTS_CCT_SchemaModule-2.1.xsd"));
        } else if (systemId.contains("UBL-CommonAggregateComponents-2.1.xsd")) {
            input.setByteStream(this.getClass().getResourceAsStream(
                    "/ehandel/common/UBL-CommonAggregateComponents-2.1.xsd"));
        } else if (systemId.contains("UBL-CommonBasicComponents-2.1.xsd")) {
            input.setByteStream(this.getClass().getResourceAsStream(
                    "/ehandel/common/UBL-CommonBasicComponents-2.1.xsd"));
        } else if (systemId.contains("UBL-CommonExtensionComponents-2.1.xsd")) {
            input.setByteStream(this.getClass().getResourceAsStream(
                    "/ehandel/common/UBL-CommonExtensionComponents-2.1.xsd"));
        } else if (systemId.contains("UBL-CommonSignatureComponents-2.1.xsd")) {
            input.setByteStream(this.getClass().getResourceAsStream(
                    "/ehandel/common/UBL-CommonSignatureComponents-2.1.xsd"));
        } else if (systemId.contains("UBL-CoreComponentParameters-2.1.xsd")) {
            input.setByteStream(this.getClass().getResourceAsStream(
                    "/ehandel/common/UBL-CoreComponentParameters-2.1.xsd"));
        } else if (systemId.contains("UBL-ExtensionContentDataType-2.1.xsd")) {
            input.setByteStream(this.getClass().getResourceAsStream(
                    "/ehandel/common/UBL-ExtensionContentDataType-2.1.xsd"));
        } else if (systemId.contains("UBL-QualifiedDataTypes-2.1.xsd")) {
            input.setByteStream(this.getClass().getResourceAsStream(
                    "/ehandel/common/UBL-QualifiedDataTypes-2.1.xsd"));
        } else if (systemId.contains("UBL-SignatureAggregateComponents-2.1.xsd")) {
            input.setByteStream(this.getClass().getResourceAsStream(
                    "/ehandel/common/UBL-SignatureAggregateComponents-2.1.xsd"));
        } else if (systemId.contains("UBL-SignatureBasicComponents-2.1.xsd")) {
            input.setByteStream(this.getClass().getResourceAsStream(
                    "/ehandel/common/UBL-SignatureBasicComponents-2.1.xsd"));
        } else if (systemId.contains("UBL-UnqualifiedDataTypes-2.1.xsd")) {
            input.setByteStream(this.getClass().getResourceAsStream(
                    "/ehandel/common/UBL-UnqualifiedDataTypes-2.1.xsd"));
        } else if (systemId.contains("UBL-XAdESv132-2.1.xsd")) {
            input.setByteStream(this.getClass().getResourceAsStream(
                    "/ehandel/common/UBL-XAdESv132-2.1.xsd"));
        } else if (systemId.contains("UBL-XAdESv141-2.1.xsd")) {
            input.setByteStream(this.getClass().getResourceAsStream(
                    "/ehandel/common/UBL-XAdESv141-2.1.xsd"));
        } else if (systemId.contains("UBL-xmldsig-core-schema-2.1.xsd")) {
            input.setByteStream(this.getClass().getResourceAsStream(
                    "/ehandel/common/UBL-xmldsig-core-schema-2.1.xsd"));
		} else {
            return parent.resolveResource(type, namespaceURI, publicId, systemId, baseURI);
        }
        returnedResources.add(systemId);
        return input;
    }

    protected class LSInputImpl implements LSInput {

        private String systemId = null;
        private InputStream byteStream = null;

        public Reader getCharacterStream() {
            return null;
        }

        public void setCharacterStream(Reader characterStream) {
        }

        public InputStream getByteStream() {

            InputStream retval = null;
            if (byteStream != null) {
                retval = byteStream;
            }
            return retval;
        }

        public void setByteStream(InputStream byteStream) {
            this.byteStream = byteStream;
        }

        public String getStringData() {
            return null;
        }

        public void setStringData(String stringData) {
        }

        public String getSystemId() {
            return systemId;
        }

        public void setSystemId(String systemId) {
            this.systemId = systemId;
        }

        public String getPublicId() {
            return null;
        }

        public void setPublicId(String publicId) {
        }

        public String getBaseURI() {
            return null;
        }

        public void setBaseURI(String baseURI) {
        }

        public String getEncoding() {
            return null;
        }

        public void setEncoding(String encoding) {
        }

        public boolean getCertifiedText() {
            return false;
        }

        public void setCertifiedText(boolean certifiedText) {
        }
    }
}
