/*
 * @(#)EHFSchemaValidatorTest.java
 *
 * Copyright (c) 2013, Zirius AS.
 * All rights reserved. 
 * 
 * Use is subject to license terms. This software is protected by 
 * copyright law and international treaties. Unauthorized reproduction or 
 * distribution of this program, or any portion of it, may result in severe 
 * civil and criminal penalties, and will be prosecuted to the maximum extent.
 */
package eu.peppol.outbound.client;

import ehandel.no.dto.ValidationInfoDTO;
import java.io.InputStream;
import org.junit.Assert;
import org.junit.Test;
import java.net.URLEncoder;
import org.apache.commons.io.IOUtils;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author vasanthis
 */
public class EHFSchemaValidatorTest {

    private String VERSION = "1.6";
    private String SCHEMA = "urn:www.cenbii.eu:profile:bii04:ver1.0#urn:www.cenbii.eu:transaction:biicoretrdm010:ver1.0:#urn:www.peppol.eu:bis:peppol4a:ver1.0#urn:www.difi.no:ehf:faktura:ver1";

    private String encodeURL(String url) throws Exception {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new Exception(e);
        }
    }

    @Test
    public void testListVersions() throws Exception {

        String result = EHFSchemaValidator.listVersions();
        System.out.println(result);
        Assert.assertNotNull(result);
    }

    @Test
    public void testListSchemasForCurrentVersion() throws Exception {

        String result = EHFSchemaValidator.listSchemasForCurrentVersion(VERSION);
        System.out.println(result);
        Assert.assertNotNull(result);
    }

    @Test
    public void testValidateVersionAndSchemaAuto() throws Exception {

        InputStream xmlStream = getClass().getResourceAsStream("/invalidInvoice.xml");

        String invoiceFileXml = new String(IOUtils.toByteArray(xmlStream));
        ValidationInfoDTO result = EHFSchemaValidator.validateVersionAndSchemaAuto(invoiceFileXml);
        System.out.println(result);
        Assert.assertNotNull(result);
    }

    @Test
    public void testValidateByVersionAndSchemaAuto() throws Exception {

        InputStream xmlStream = getClass().getResourceAsStream("/invalidInvoice.xml");

        String invoiceFileXml = new String(IOUtils.toByteArray(xmlStream));
        String result = EHFSchemaValidator.validateByVersionAndSchemaAuto(invoiceFileXml);
        System.out.println(result);
        Assert.assertNotNull(result);
    }

    @Test
    public void testValidateAutoSuppressWarnings() throws Exception {

        InputStream xmlStream = getClass().getResourceAsStream("/invalidInvoice.xml");

        String invoiceFileXml = new String(IOUtils.toByteArray(xmlStream));
        String result = EHFSchemaValidator.validateVersionAndSchemaAutoSuppressWarnings(invoiceFileXml);
        System.out.println(result);
        Assert.assertNotNull(result);
    }

    @Test
    public void testValidateAutoFilterWarnings() throws Exception {

        InputStream xmlStream = getClass().getResourceAsStream("/invalidInvoice.xml");

        String invoiceFileXml = new String(IOUtils.toByteArray(xmlStream));
        ValidationInfoDTO result = EHFSchemaValidator.validateVersionAndSchemaAutoFilterWarnings(invoiceFileXml);
        System.out.println(result);
        Assert.assertNotNull(result);
    }

    @Test
    public void testValidateBySchema() throws Exception {

        InputStream xmlStream = getClass().getResourceAsStream("/invalidInvoice.xml");

        String invoiceFileXml = new String(IOUtils.toByteArray(xmlStream));
        String result = EHFSchemaValidator.validateBySchema(VERSION, encodeURL(SCHEMA), invoiceFileXml);
        System.out.println(result);
        Assert.assertNotNull(result);
    }

    @Test
    public void testValidateSchema() throws Exception {

        InputStream xmlStream = getClass().getResourceAsStream("/invalidInvoice.xml");

        String invoiceFileXml = new String(IOUtils.toByteArray(xmlStream));
        ValidationInfoDTO result = EHFSchemaValidator.validateSchema(VERSION, encodeURL(SCHEMA), invoiceFileXml);
        System.out.println(result);
        Assert.assertNotNull(result);
    }

    @Test
    public void testValidateSchemaSuppressWarnings() throws Exception {

        InputStream xmlStream = getClass().getResourceAsStream("/invalidInvoice.xml");

        String invoiceFileXml = new String(IOUtils.toByteArray(xmlStream));
        String result = EHFSchemaValidator.validateSchemaSuppressWarnings(VERSION, encodeURL(SCHEMA), invoiceFileXml);
        System.out.println(result);
        Assert.assertNotNull(result);
    }

    @Test
    public void testValidateSchemaFilterWarnings() throws Exception {

        InputStream xmlStream = getClass().getResourceAsStream("/invalidInvoice.xml");

        String invoiceFileXml = new String(IOUtils.toByteArray(xmlStream));
        ValidationInfoDTO result = EHFSchemaValidator.validateSchemaFilterWarnings(VERSION, encodeURL(SCHEMA), invoiceFileXml);
        System.out.println(result);
        Assert.assertNotNull(result);
    }
}