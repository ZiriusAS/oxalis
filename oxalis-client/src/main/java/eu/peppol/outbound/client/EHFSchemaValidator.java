/*
 * @(#)EHFSchemaValidator.java
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.io.IOUtils;

import ehandel.no.dto.ValidationInfoDTO;
import ehandel.no.util.StringUtils;

/**
 * Utility class to access validation services via http client.
 * 
 * @author vasanthis
 */
public final class EHFSchemaValidator {

    private static final String VALIDATOR_CLIENT_PROPS_PATH = "/ap.client.properties";
    private static final String DEFAULT_VALIDATOR_CLIENT_PROPS_PATH = "/default.ap.client.properties";
    private static final String KEY_VALIDATOR_URL = "validator.client.url";
    private static final String CONTENT_TYPE = "application/xml";
    private static final String SUPPRESSWARNINGS = "suppresswarnings";
    private static final String EXCEPTION_STRING = "Exception from EHF Validation Server :";
    private static String VALIDATOR_URL = "";

    private static String METHOD_VALIDATE_EHF_XML = "validateEhfXml";

    static {

        InputStream inputStream = null;
        try {

            Properties props = new Properties();
            URL url = EHFSchemaValidator.class.getResource(VALIDATOR_CLIENT_PROPS_PATH);
            if (url != null) {
                inputStream = url.openStream();
                props.load(inputStream);
            } else {
                props.load(EHFSchemaValidator.class.getResourceAsStream(DEFAULT_VALIDATOR_CLIENT_PROPS_PATH));
            }
            VALIDATOR_URL = props.getProperty(KEY_VALIDATOR_URL);
        } catch (IOException e) {
            throw new RuntimeException("No properties loaded from " + VALIDATOR_CLIENT_PROPS_PATH
                    + ", file was not found in classpath");
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    private EHFSchemaValidator() {
    }
    
    public static void setClientUrl(String url) {
    	VALIDATOR_URL = url;
    }

    private static PostMethod getHttpPostMethod(String method) {

        String url = getValidationURL(method);

        PostMethod httpPost = new PostMethod(url);
        return httpPost;
    }

    private static GetMethod getHttpGetMethod(String method) {

        String url = getValidationURL(method);

        GetMethod httpGet = new GetMethod(url);
        return httpGet;
    }
    
    private static String getValidationURL(String method) {
        return StringUtils.isEmpty(method) ? VALIDATOR_URL : VALIDATOR_URL + "/" + method;
    }

    private static HttpClient getHttpClient() {

        HttpClient httpClient = new HttpClient();
        return httpClient;
    }

    private static ByteArrayInputStream streamObject(String str) throws IOException {

        return new ByteArrayInputStream(str.getBytes());
    }

    private static String getTextMessage(InputStream is) throws IOException {

        StringWriter writer = new StringWriter();
        IOUtils.copy(is, writer);

        return writer.toString();
    }

    private static String getExceptionMessage(InputStream is) throws IOException {

        StringWriter writer = new StringWriter();
        IOUtils.copy(is, writer);

        return EXCEPTION_STRING + writer.toString();
    }

    /**
     * validate EHF XML document
     * @param xml
     * @return
     * @throws IOException
     * @throws Exception 
     */
    public static String validateEhfXml(String xml) throws IOException, Exception {

        HttpClient httpClient = getHttpClient();
        PostMethod httpPost = getHttpPostMethod(METHOD_VALIDATE_EHF_XML);

        try {

            RequestEntity requestEntity = new InputStreamRequestEntity(streamObject(xml), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);
            int status = httpClient.executeMethod(httpPost);

            if (status == HttpStatus.SC_OK) {
                return getTextMessage(httpPost.getResponseBodyAsStream());
            }
            throw new Exception(getExceptionMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }

    /**
     * List versions.
     * 
     * @return the string
     * @throws ClassNotFoundException
     *             the class not found exception
     * @throws Exception
     *             the exception
     */
    @Deprecated
    public static String listVersions() throws ClassNotFoundException, Exception {

        HttpClient httpClient = getHttpClient();
        GetMethod httpGet = getHttpGetMethod(null);

        try {

            int status = httpClient.executeMethod(httpGet);
            if (status == HttpStatus.SC_OK) {
                return getTextMessage(httpGet.getResponseBodyAsStream());
            }
            throw new Exception(getExceptionMessage(httpGet.getResponseBodyAsStream()));
        } finally {
            httpGet.releaseConnection();
        }
    }

    /**
     * List schemas for current version.
     * 
     * @param version
     *            the version
     * @return the string
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws Exception
     *             the exception
     */
    @Deprecated
    public static String listSchemasForCurrentVersion(String version) throws IOException, Exception {

        HttpClient httpClient = getHttpClient();
        GetMethod httpGet = getHttpGetMethod(version);

        try {

            int status = httpClient.executeMethod(httpGet);
            if (status == HttpStatus.SC_OK) {
                return getTextMessage(httpGet.getResponseBodyAsStream());
            }
            throw new Exception(getExceptionMessage(httpGet.getResponseBodyAsStream()));
        } finally {
            httpGet.releaseConnection();
        }
    }

    /**
     * Validate by version and schema auto.
     * 
     * @param xml
     *            the xml
     * @return the string
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws Exception
     *             the exception
     */
    @Deprecated
    public static String validateByVersionAndSchemaAuto(String xml) throws IOException,
            Exception {

        HttpClient httpClient = getHttpClient();
        PostMethod httpPost = getHttpPostMethod(null);

        try {

            RequestEntity requestEntity = new InputStreamRequestEntity(streamObject(xml), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);
            int status = httpClient.executeMethod(httpPost);

            if (status == HttpStatus.SC_OK) {
                return getTextMessage(httpPost.getResponseBodyAsStream());
            }
            throw new Exception(getExceptionMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }

    /**
     * Validate version and schema auto suppress warnings.
     * 
     * @param xml
     *            the xml
     * @return the string
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws Exception
     *             the exception
     */
    @Deprecated
    public static String validateVersionAndSchemaAutoSuppressWarnings(String xml)
            throws IOException, Exception {

        HttpClient httpClient = getHttpClient();
        PostMethod httpPost = getHttpPostMethod(SUPPRESSWARNINGS);

        try {

            RequestEntity requestEntity = new InputStreamRequestEntity(streamObject(xml), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);
            int status = httpClient.executeMethod(httpPost);

            if (status == HttpStatus.SC_OK) {
                return getTextMessage(httpPost.getResponseBodyAsStream());

            }
            throw new Exception(getExceptionMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }

    /**
     * Validate By schema.
     * 
     * @param version
     *            the version
     * @param schema
     *            the schema
     * @param xml
     *            the xml
     * @return the string
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws Exception
     *             the exception
     */
    @Deprecated
    public static String validateBySchema(String version, String schema, String xml)
            throws IOException, Exception {

        HttpClient httpClient = getHttpClient();
        PostMethod httpPost = getHttpPostMethod(version + "/" + schema);

        try {

            RequestEntity requestEntity = new InputStreamRequestEntity(streamObject(xml), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);
            int status = httpClient.executeMethod(httpPost);

            if (status == HttpStatus.SC_OK) {
                return getTextMessage(httpPost.getResponseBodyAsStream());

            }
            throw new Exception(getExceptionMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }

    /**
     * Validate schema suppress warnings.
     * 
     * @param version
     *            the version
     * @param schema
     *            the schema
     * @param xml
     *            the xml
     * @return the string
     * @throws Exception
     *             the exception
     */
    @Deprecated
    public static String validateSchemaSuppressWarnings(String version, String schema, String xml)
            throws Exception {

        HttpClient httpClient = getHttpClient();
        PostMethod httpPost =
                getHttpPostMethod(version + "/" + schema + "/" + SUPPRESSWARNINGS);

        try {

            RequestEntity requestEntity = new InputStreamRequestEntity(streamObject(xml), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);
            int status = httpClient.executeMethod(httpPost);

            if (status == HttpStatus.SC_OK) {
                return getTextMessage(httpPost.getResponseBodyAsStream());

            }
            throw new Exception(getExceptionMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }

    /**
     * Validate version and schema auto.
     * 
     * @param xml
     *            the ValidationInfoDTO
     * @return the ValidationInfoDTO
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws Exception
     *             the exception
     */
    @Deprecated
    public static ValidationInfoDTO validateVersionAndSchemaAuto(String xml) throws IOException,
            Exception {

        HttpClient httpClient = getHttpClient();
        PostMethod httpPost = getHttpPostMethod(null);

        try {

            RequestEntity requestEntity = new InputStreamRequestEntity(streamObject(xml), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);
            int status = httpClient.executeMethod(httpPost);

            if (status == HttpStatus.SC_OK) {
                return getValidationInfo(getTextMessage(httpPost.getResponseBodyAsStream()));
            }
            return getValidationInfo(getExceptionMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }

    /**
     * Validate version and schema auto filter warnings.
     * 
     * @param xml
     *            the xml
     * @return the ValidationInfoDTO
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws Exception
     *             the exception
     */
    @Deprecated
    public static ValidationInfoDTO validateVersionAndSchemaAutoFilterWarnings(String xml)
            throws IOException, Exception {

        HttpClient httpClient = getHttpClient();
        PostMethod httpPost = getHttpPostMethod(SUPPRESSWARNINGS);

        try {

            RequestEntity requestEntity = new InputStreamRequestEntity(streamObject(xml), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);
            int status = httpClient.executeMethod(httpPost);

            if (status == HttpStatus.SC_OK) {
                return getValidationInfo(getTextMessage(httpPost.getResponseBodyAsStream()));

            }
            throw new Exception(getExceptionMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }

    /**
     * Validate schema.
     * 
     * @param version
     *            the version
     * @param schema
     *            the schema
     * @param xml
     *            the xml
     * @return the ValidationInfoDTO
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws Exception
     *             the exception
     */
    @Deprecated
    public static ValidationInfoDTO validateSchema(String version, String schema, String xml)
            throws IOException, Exception {

        HttpClient httpClient = getHttpClient();
        PostMethod httpPost = getHttpPostMethod(version + "/" + schema);

        try {

            RequestEntity requestEntity = new InputStreamRequestEntity(streamObject(xml), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);
            int status = httpClient.executeMethod(httpPost);

            if (status == HttpStatus.SC_OK) {
                return getValidationInfo(getTextMessage(httpPost.getResponseBodyAsStream()));

            }
            throw new Exception(getExceptionMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }

    /**
     * Validate schema filter warnings.
     * 
     * @param version
     *            the version
     * @param schema
     *            the schema
     * @param xml
     *            the xml
     * @return the ValidationInfoDTO
     * @throws Exception
     *             the exception
     */
    @Deprecated
    public static ValidationInfoDTO validateSchemaFilterWarnings(String version, String schema, String xml)
            throws Exception {

        HttpClient httpClient = getHttpClient();
        PostMethod httpPost =
                getHttpPostMethod(version + "/" + schema + "/" + SUPPRESSWARNINGS);

        try {

            RequestEntity requestEntity = new InputStreamRequestEntity(streamObject(xml), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);
            int status = httpClient.executeMethod(httpPost);

            if (status == HttpStatus.SC_OK) {
                return getValidationInfo(getTextMessage(httpPost.getResponseBodyAsStream()));

            }
            throw new Exception(getExceptionMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }

    private static ValidationInfoDTO getValidationInfo(String xml) throws JAXBException, IOException {
        
        Unmarshaller unMarshaller = getUnMarshaller();
        ValidationInfoDTO validationInfoDTO = (ValidationInfoDTO) unMarshaller.unmarshal(IOUtils.toInputStream(xml, "UTF-8"));
        return validationInfoDTO;
    }

    private static Unmarshaller getUnMarshaller() throws JAXBException {

        final JAXBContext context = JAXBContext.newInstance(ValidationInfoDTO.class);
        return context.createUnmarshaller();
    }
}