/*
 * @(#)AccessPointClient.java
 *
 * Copyright (c) 2012, Zirius AS.
 * All rights reserved. 
 * 
 * Use is subject to license terms. This software is protected by 
 * copyright law and international treaties. Unauthorized reproduction or 
 * distribution of this program, or any portion of it, may result in severe 
 * civil and criminal penalties, and will be prosecuted to the maximum extent.
 */
package eu.peppol.outbound.client;

import ehandel.no.EHFCustomizationID;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import ehandel.no.dto.AccesspointDetails;
import eu.peppol.outbound.api.ReceiptDTO;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.io.IOUtils;

import eu.peppol.outbound.api.DocumentDTO;
import eu.peppol.outbound.api.EmailDTO;
import eu.peppol.outbound.api.MessageDTO;
import eu.peppol.outbound.api.MessageIdListDTO;
import eu.peppol.outbound.api.MessageListDTO;
import eu.peppol.outbound.api.MessageRemoverDTO;
import eu.peppol.outbound.api.UserDTO;
import java.net.URLEncoder;
import org.apache.commons.httpclient.params.HttpParams;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.params.CoreConnectionPNames;

/**
 * Utility class to access outbound services via http client.
 *
 * @author senthilkumarn
 */
public final class AccessPointClient {

    private static final String AP_CLIENT_PROPS_PATH = "/ap.client.properties";
    private static final String DEFAULT_AP_CLIENT_PROPS_PATH = "/default.ap.client.properties";
    private static final String KEY_ACCESSPOINT_URL = "ap.client.url";
    private static final String KEY_USERNAME = "ap.client.username";
    private static final String KEY_PASSWORD = "ap.client.password";
    private static final String REALM_NAME = "AccessPoint Auth";
    private static final String CONTENT_TYPE = "application/octet-stream";
    private static final String METHOD_SEND = "send";
    private static final String METHOD_SEND_ORDER = "sendOrder";
    private static final String METHOD_SEND_ORDER_RESPONSE = "sendOrderResponse";
    private static final String METHOD_RECEIVE = "receive";
    private static final String METHOD_RECEIVE_MESSAGE_ID = "receiveMessageId";
    private static final String METHOD_RECEIVE_UN_READ_MESSAGE_ID_FOR_WEB = "receiveUnReadMessageIdForWeb";
    private static final String METHOD_GET_MESSAGE_INFO = "getMessageInfo";
    private static final String METHOD_FETCH = "fetch";
    private static final String METHOD_FETCH_MESSAGE_ID = "fetchMessageId";
    private static final String METHOD_REMOVE = "remove";
    private static final String METHOD_VALIDATE = "validate";
    private static final String METHOD_VALIDATE_AGAINST_CUSTOMIZATION_ID = "validateAgainstCustomizationID";
    private static final String METHOD_MARK_AS_READ = "markAsRead";
    private static final String METHOD_MARK_AS_READ_FROM_WEB = "markAsReadFromWeb";
    private static final String METHOD_CONFIGURE_PARTICIPANT_EMAIL = "configureParticipantEmail";
    private static final String METHOD_GET_PARTICIPANT_EMAIL = "getParticipantEmail";
    private static final String METHOD_CREATE_USER = "createUser";
    private static final String METHOD_GET_ACCESSPOINT_DETAILS = "getAccesspointDetails";
    private static final String METHOD_GET_IF_PARTICIPANT_ENABLED_EHFV3_INVOICE = "getEHFV3InvoiceEndPoint";
    private static final String METHOD_GET_IF_PARTICIPANT_ENABLED_EHFV3_CREDITNOTE = "getEHFV3CreditNoteEndPoint";
    private static final String METHOD_SEND_EPEPPOL = "sendEPEPPOL";
    private static final String METHOD_GET_EPEPPOL_RECEIPTS = "getEPEPPOLReceipts";    
    private static final String METHOD_GET_EPEPPOL_LAST_RECEIPTS = "getLastEPEPPOLReceipts"; 
    private static final String METHOD_GET_ALL_NEW_RECEIPTS = "getAllNewReceipts"; 
    private static final String METHOD_MARK_RECEIPTS = "markReceiptAsRead"; 
    private static final String METHOD_SEND_EHFV3_ORDER = "sendEHFV3Order";
    private static final String METHOD_SEND_EHFV3_ORDER_RESPONSE = "sendEHFV3OrderResponse";
    private static final String METHOD_RECEIVE_UN_READ_MESSAGE_ID_OF_ORDER = "receiveUnReadMessageIdOfOrder";
    private static final String METHOD_RECEIVE_UN_READ_MESSAGE_ID_OF_ORDER_RESPONSE = "receiveUnReadMessageIdOfOrderResponse";
    private static final String METHOD_GET_IF_PARTICIPANT_ENABLED_EHFV3_ORDER = "getEHFV3OrderEndPoint";
    private static final String METHOD_GET_IF_PARTICIPANT_ENABLED_EHFV3_ORDER_RESPONSE = "getEHFV3OrderResponseEndPoint";
    private static final String EXCEPTION_STRING = "Exception from EHF Server :";
    private static String ACCESSPOINT_URL = "";
    private static String USERNAME = "";
    private static String PASSWORD = "";

    static {

        InputStream inputStream = null;
        try {

            Properties props = new Properties();
            URL url = AccessPointClient.class.getResource(AP_CLIENT_PROPS_PATH);
            if (url != null) {
                inputStream = url.openStream();
                props.load(inputStream);
            } else {
                props.load(AccessPointClient.class.getResourceAsStream(DEFAULT_AP_CLIENT_PROPS_PATH));
            }
            ACCESSPOINT_URL = props.getProperty(KEY_ACCESSPOINT_URL);
            USERNAME = props.getProperty(KEY_USERNAME);
            PASSWORD = props.getProperty(KEY_PASSWORD);
        } catch (IOException e) {
            throw new RuntimeException("No properties loaded from " + AP_CLIENT_PROPS_PATH
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

    private AccessPointClient() {
    }

    private static HttpClient getHttpClient(String userName, String password) {

        HttpClient httpClient = new HttpClient();
        httpClient.getState().setCredentials(
                new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, REALM_NAME),
                new UsernamePasswordCredentials(userName, password));
        
        int timeout = 60 * 30; // minutes
        
        HttpParams httpParams = httpClient.getParams();
        httpParams.setParameter(
                CoreConnectionPNames.CONNECTION_TIMEOUT, timeout * 1000);
        httpParams.setParameter(
          CoreConnectionPNames.SO_TIMEOUT, timeout * 1000);
        httpParams.setParameter(
                ClientPNames.CONN_MANAGER_TIMEOUT, new Long(timeout * 1000));

        return httpClient;
    }

    private static PostMethod getHttpPostMethod(String method) {

        PostMethod httpPost = new PostMethod(ACCESSPOINT_URL + "/" + method);
        httpPost.setDoAuthentication(true);

        return httpPost;
    }

    private static GetMethod getHttpGetMethod(String method) {

        GetMethod httpGet = new GetMethod(ACCESSPOINT_URL + "/" + method);
        httpGet.setDoAuthentication(true);

        return httpGet;
    }

    public static void setClientUrl(String url) {
        ACCESSPOINT_URL = url;
    }

    /**
     * Send invoice.
     *
     * @param documentDTO the document dto
     * @param userName the user name
     * @param password the password
     * @return the string message id
     * @throws Exception the exception
     * @deprecated it is recommended to use enhanced peppol function to transmit documents. 
     */
    @Deprecated
    public static String sendInvoice(DocumentDTO documentDTO, String userName, String password)
            throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        PostMethod httpPost = getHttpPostMethod(METHOD_SEND);

        try {

            RequestEntity requestEntity
                    = new InputStreamRequestEntity(streamObject(documentDTO), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);
            int status = httpClient.executeMethod(httpPost);

            if (status == HttpStatus.SC_OK) {
                return (String) new ObjectInputStream(httpPost.getResponseBodyAsStream()).readObject();
            }

            throw new Exception(getTextMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }

    /**
     * Send invoice.
     *
     * @param documentDTO the document dto
     * @return the string
     * @throws Exception the exception
     * @deprecated it is recommended to use enhanced peppol function to transmit documents. 
     */
    @Deprecated
    public static String sendInvoice(DocumentDTO documentDTO) throws Exception {
        return sendInvoice(documentDTO, USERNAME, PASSWORD);
    }

    private static ByteArrayInputStream streamObject(Object obj) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.flush();
        oos.close();

        return new ByteArrayInputStream(baos.toByteArray());
    }

    private static String getTextMessage(InputStream is) throws IOException {

        StringWriter writer = new StringWriter();
        IOUtils.copy(is, writer);
        return EXCEPTION_STRING + writer.toString();
    }

    /**
     * Receive invoices based on the date.
     *
     * @param syncDate the sync date
     * @param userName the user name
     * @param password the password
     * @return the list
     * @throws Exception the exception
     */
    @Deprecated
    public static MessageListDTO receiveInvoice(Date syncDate, String userName, String password)
            throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        PostMethod httpPost = getHttpPostMethod(METHOD_RECEIVE);

        try {

            RequestEntity requestEntity
                    = new InputStreamRequestEntity(streamObject(syncDate), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);
            int status = httpClient.executeMethod(httpPost);

            if (status == HttpStatus.SC_OK) {
                return (MessageListDTO) new ObjectInputStream(httpPost.getResponseBodyAsStream()).readObject();
            }
            throw new Exception(getTextMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }

    /**
     * Receive invoice.
     *
     * @param syncDate the sync date
     * @return the message list dto
     * @throws Exception the exception
     */
    @Deprecated
    public static MessageListDTO receiveInvoice(Date syncDate) throws Exception {
        return receiveInvoice(syncDate, USERNAME, PASSWORD);
    }

    /**
     * Receive invoices based on the participant.
     *
     * @param participant the participant
     * @param userName the user name
     * @param password the password
     * @return the list
     * @throws Exception the exception
     */
    @Deprecated
    public static MessageListDTO receiveInvoice(String participant, String userName, String password)
            throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        GetMethod httpGet = getHttpGetMethod(METHOD_FETCH + "/" + participant);

        try {

            int status = httpClient.executeMethod(httpGet);

            if (status == HttpStatus.SC_OK) {
                return (MessageListDTO) new ObjectInputStream(httpGet.getResponseBodyAsStream()).readObject();
            }
            throw new Exception(getTextMessage(httpGet.getResponseBodyAsStream()));
        } finally {
            httpGet.releaseConnection();
        }
    }

    /**
     * Receive invoice.
     *
     * @param participant the participant
     * @return the message list dto
     * @throws Exception the exception
     */
    @Deprecated
    public static MessageListDTO receiveInvoice(String participant) throws Exception {
        return receiveInvoice(participant, USERNAME, PASSWORD);
    }

    /**
     * Get Message
     *
     * @param messageId
     * @param userName
     * @param password
     * @return
     * @throws Exception
     */
    public static MessageDTO getMessage(String messageId, String userName, String password) throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        GetMethod httpGet = getHttpGetMethod(METHOD_GET_MESSAGE_INFO + "/" + messageId);

        try {

            int status = httpClient.executeMethod(httpGet);

            if (status == HttpStatus.SC_OK) {
                return (MessageDTO) new ObjectInputStream(httpGet.getResponseBodyAsStream()).readObject();
            }
            throw new Exception(getTextMessage(httpGet.getResponseBodyAsStream()));
        } finally {
            httpGet.releaseConnection();
        }
    }

    /**
     * Get Message
     *
     * @param messageId
     * @return
     * @throws Exception
     */
    public static MessageDTO getMessage(String messageId) throws Exception {
        return getMessage(messageId, USERNAME, PASSWORD);
    }

    /**
     * Get Message Id
     *
     * @param participant
     * @param userName
     * @param password
     * @return
     * @throws Exception
     */
    public static MessageIdListDTO getMessageId(String participant, String userName, String password)
            throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        GetMethod httpGet = getHttpGetMethod(METHOD_FETCH_MESSAGE_ID + "/" + participant);

        try {

            int status = httpClient.executeMethod(httpGet);

            if (status == HttpStatus.SC_OK) {
                return (MessageIdListDTO) new ObjectInputStream(httpGet.getResponseBodyAsStream()).readObject();
            }
            throw new Exception(getTextMessage(httpGet.getResponseBodyAsStream()));
        } finally {
            httpGet.releaseConnection();
        }
    }

    /**
     * Get Message Id
     *
     * @param participant
     * @return
     * @throws Exception
     */
    public static MessageIdListDTO getMessageId(String participant) throws Exception {

        return getMessageId(participant, USERNAME, PASSWORD);
    }

    /**
     * Get Message Id
     *
     * @param syncDate
     * @param userName
     * @param password
     * @return
     * @throws Exception
     */
    public static MessageIdListDTO getMessageId(Date syncDate, String userName, String password)
            throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        PostMethod httpPost = getHttpPostMethod(METHOD_RECEIVE_MESSAGE_ID);

        try {

            RequestEntity requestEntity
                    = new InputStreamRequestEntity(streamObject(syncDate), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);
            int status = httpClient.executeMethod(httpPost);

            if (status == HttpStatus.SC_OK) {
                return (MessageIdListDTO) new ObjectInputStream(httpPost.getResponseBodyAsStream()).readObject();
            }
            throw new Exception(getTextMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }

    /**
     * Get un Read Message Id for Web
     *
     * @param syncDate
     * @param userName
     * @param password
     * @return
     * @throws Exception
     */
    public static MessageIdListDTO getUnReadMessageIdForWeb(String userName, String password)
            throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        PostMethod httpPost = getHttpPostMethod(METHOD_RECEIVE_UN_READ_MESSAGE_ID_FOR_WEB + "/");

        try {

            int status = httpClient.executeMethod(httpPost);

            if (status == HttpStatus.SC_OK) {
                return (MessageIdListDTO) new ObjectInputStream(httpPost.getResponseBodyAsStream()).readObject();
            }
            throw new Exception(getTextMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }

    /**
     * Get un Read Message Id for Web
     *
     * @return MessageIdListDTO
     * @throws Exception
     */
    public static MessageIdListDTO getUnReadMessageIdForWeb() throws Exception {
        return getUnReadMessageIdForWeb(USERNAME, PASSWORD);
    }

    /**
     * Get Message Id
     *
     * @param syncDate
     * @return
     * @throws Exception
     */
    public static MessageIdListDTO getMessageId(Date syncDate) throws Exception {

        return getMessageId(syncDate, USERNAME, PASSWORD);
    }

    /**
     * Removes the message of a participant.
     *
     * @param msgRemoverDTO the msg remover dto
     * @param userName the user name
     * @param password the password
     * @return true, if successful
     * @throws Exception the exception
     */
    public static boolean removeMessage(MessageRemoverDTO msgRemoverDTO, String userName,
            String password) throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        PostMethod httpPost = getHttpPostMethod(METHOD_REMOVE);

        try {

            RequestEntity requestEntity
                    = new InputStreamRequestEntity(streamObject(msgRemoverDTO), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);
            int status = httpClient.executeMethod(httpPost);

            if (status == HttpStatus.SC_OK) {
                return (Boolean) new ObjectInputStream(httpPost.getResponseBodyAsStream()).readObject();
            }
            throw new Exception(getTextMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }

    /**
     * Removes the message.
     *
     * @param msgRemoverDTO the msg remover dto
     * @return true, if successful
     * @throws Exception the exception
     */
    public static boolean removeMessage(MessageRemoverDTO msgRemoverDTO) throws Exception {
        return removeMessage(msgRemoverDTO, USERNAME, PASSWORD);
    }

    /**
     * Validates the participant for given profile ID
     *
     * @param participant the participant
     * @param userName the user name
     * @param password the password
     * @return true, if is valid participant
     * @throws Exception the exception
     */
    public static boolean isValidParticipant(String participant, String userName, String password, EHFCustomizationID customizationID)
            throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        GetMethod httpGet = getHttpGetMethod(METHOD_VALIDATE_AGAINST_CUSTOMIZATION_ID + "/" + participant + URLEncoder.encode("^" + customizationID.getValue(), "UTF-8"));

        try {

            int status = httpClient.executeMethod(httpGet);

            if (status == HttpStatus.SC_OK) {
                return (Boolean) new ObjectInputStream(httpGet.getResponseBodyAsStream()).readObject();
            }
            throw new Exception(getTextMessage(httpGet.getResponseBodyAsStream()));
        } finally {
            httpGet.releaseConnection();
        }
    }

    /**
     * Checks for valid participant. This method is depricated as the validation
     * happens only for EHF invoice(1.6 and 2.0) Validation does not happen for
     * CreditNote and Order. Instead use "isValidParticipant(String participant,
     * String userName, String password, EHFCustomizationID customizationID)"
     *
     * @param participant the participant
     * @param userName the user name
     * @param password the password
     * @return true, if is valid participant
     * @throws Exception the exception
     */
    @Deprecated
    public static boolean isValidParticipant(String participant, String userName, String password)
            throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        GetMethod httpGet = getHttpGetMethod(METHOD_VALIDATE + "/" + participant);

        try {

            int status = httpClient.executeMethod(httpGet);

            if (status == HttpStatus.SC_OK) {
                return (Boolean) new ObjectInputStream(httpGet.getResponseBodyAsStream()).readObject();
            }
            throw new Exception(getTextMessage(httpGet.getResponseBodyAsStream()));
        } finally {
            httpGet.releaseConnection();
        }
    }

    /**
     * Checks for valid participant. This method is depricated as the validation
     * happens only for EHF invoice(1.6 and 2.0) Validation does not happen for
     * CreditNote and Order. Instead use "isValidParticipant(String participant,
     * EHFCustomizationID customizationID)"
     *
     * @param participant the participant
     * @return true, if is valid participant
     * @throws Exception the exception
     */
    @Deprecated
    public static boolean isValidParticipant(String participant) throws Exception {
        return isValidParticipant(participant, USERNAME, PASSWORD);
    }

    /**
     * Checks if is valid participant against the customizationID.
     *
     * @param participant the participant
     * @return true, if is valid participant
     * @throws Exception the exception
     */
    public static boolean isValidParticipant(String participant, EHFCustomizationID customizationID) throws Exception {
        return isValidParticipant(participant, USERNAME, PASSWORD, customizationID);
    }

    /**
     * Mark message as read.
     *
     * @param messageIds the message ids
     * @param userName the user name
     * @param password the password
     * @return true, if successful
     * @throws Exception the exception
     */
    public static boolean markAsRead(List<String> messageIds, String userName, String password)
            throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        PostMethod httpPost = getHttpPostMethod(METHOD_MARK_AS_READ);

        try {

            RequestEntity requestEntity
                    = new InputStreamRequestEntity(streamObject(messageIds), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);
            int status = httpClient.executeMethod(httpPost);

            if (status == HttpStatus.SC_OK) {
                return (Boolean) new ObjectInputStream(httpPost.getResponseBodyAsStream()).readObject();
            }
            throw new Exception(getTextMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }

    /**
     * Mark as read.
     *
     * @param messageIds the message ids
     * @return true, if successful
     * @throws Exception the exception
     */
    public static boolean markAsRead(List<String> messageIds) throws Exception {
        return markAsRead(messageIds, USERNAME, PASSWORD);
    }

    /**
     * Mark message as read from web.
     *
     * @param messageIds the message ids
     * @param userName the user name
     * @param password the password
     * @return true, if successful
     * @throws Exception the exception
     */
    public static boolean markAsReadFromWeb(List<String> messageIds, String userName, String password)
            throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        PostMethod httpPost = getHttpPostMethod(METHOD_MARK_AS_READ_FROM_WEB);

        try {

            RequestEntity requestEntity
                    = new InputStreamRequestEntity(streamObject(messageIds), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);
            int status = httpClient.executeMethod(httpPost);

            if (status == HttpStatus.SC_OK) {
                return (Boolean) new ObjectInputStream(httpPost.getResponseBodyAsStream()).readObject();
            }
            throw new Exception(getTextMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }

    /**
     * Mark as read from web.
     *
     * @param messageIds the message ids
     * @return true, if successful
     * @throws Exception the exception
     */
    public static boolean markAsReadFromWeb(List<String> messageIds) throws Exception {
        return markAsReadFromWeb(messageIds, USERNAME, PASSWORD);
    }

    /**
     * Configure participant email.
     *
     * @param emailDTOs the email dtos
     * @param userName the user name
     * @param password the password
     * @return true, if successful
     * @throws Exception the exception
     */
    public static boolean configureParticipantEmail(List<EmailDTO> emailDTOs, String userName,
            String password) throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        PostMethod httpPost = getHttpPostMethod(METHOD_CONFIGURE_PARTICIPANT_EMAIL);

        try {

            RequestEntity requestEntity
                    = new InputStreamRequestEntity(streamObject(emailDTOs), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);
            int status = httpClient.executeMethod(httpPost);

            if (status == HttpStatus.SC_OK) {
                return (Boolean) new ObjectInputStream(httpPost.getResponseBodyAsStream()).readObject();
            }
            throw new Exception(getTextMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }

    /**
     * Configure participant email.
     *
     * @param emailDTOs the email dt os
     * @return true, if successful
     * @throws Exception the exception
     */
    public static boolean configureParticipantEmail(List<EmailDTO> emailDTOs) throws Exception {
        return configureParticipantEmail(emailDTOs, USERNAME, PASSWORD);
    }

    /**
     * Configure participant email.
     *
     * @param participantId the participant Id
     * @param userName the user name
     * @param password the password
     * @return the participant string[]
     * @throws Exception the exception
     */
    public static String[] getParticipantEmail(String participantId, String userName,
            String password) throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        PostMethod httpPost = getHttpPostMethod(METHOD_GET_PARTICIPANT_EMAIL);

        try {

            RequestEntity requestEntity
                    = new InputStreamRequestEntity(streamObject(participantId), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);
            int status = httpClient.executeMethod(httpPost);

            if (status == HttpStatus.SC_OK) {
                return (String[]) new ObjectInputStream(httpPost.getResponseBodyAsStream()).readObject();
            }
            throw new Exception(getTextMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }

    /**
     * Get the access point details
     *
     * @param participantId
     * @return the AccesspointDetails
     * @throws Exception
     */
    public static AccesspointDetails getAccesspointDetails(String participantId) throws Exception {
        return getAccesspointDetails(participantId, USERNAME, PASSWORD);
    }

    /**
     * Get the access point details
     *
     * @param participantId
     * @param userName
     * @param password
     * @return the AccesspointDetails
     * @throws Exception
     */
    public static AccesspointDetails getAccesspointDetails(String participantId, String userName, String password) throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        PostMethod httpPost = getHttpPostMethod(METHOD_GET_ACCESSPOINT_DETAILS);

        try {

            RequestEntity requestEntity
                    = new InputStreamRequestEntity(streamObject(participantId), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);

            int status = httpClient.executeMethod(httpPost);
            if (status == HttpStatus.SC_OK) {

                String endpointData = httpPost.getResponseBodyAsString();
                endpointData = endpointData.replace("{", "");
                endpointData = endpointData.replace("}", "");
                String[] endpointArray = endpointData.split(",");
                AccesspointDetails accesspointDetails = new AccesspointDetails();

                if (endpointArray.length >= 3) {

                    accesspointDetails.setUrl(endpointArray[0].split("=")[1]);
                    accesspointDetails.setBusDoxProtocol(endpointArray[1].split("=")[1]);
                    accesspointDetails.setCommonName(endpointArray[2].split("=")[1]);
                }
                return accesspointDetails;
            }
            throw new Exception(getTextMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }

    /**
     * Return true if EHFV3 Invoice is enabled
     *
     * @param participantId
     * @return the boolean
     * 
     */
    public static boolean isParticipantEHFV3Enabled(String participantId) throws Exception {

        try {

            return getEHFV3InvoiceEndPoint(participantId) != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    
    /**
     * Return true if EHFV3 Invoice is enabled
     *
     * @param participantId
     * @param userName 
     * @param password
     * @return the boolean
     * 
     */
    public static boolean isParticipantEHFV3Enabled(String participantId, String userName, String password) throws Exception {

        try {

            return getEHFV3InvoiceEndPoint(participantId, userName, password) != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the access point details if EHFV3 Invoice is enabled
     *
     * @param participantId
     * @return the AccesspointDetails
     * @throws Exception
     */
    public static AccesspointDetails getEHFV3InvoiceEndPoint(String participantId) throws Exception {
        return getEHFV3InvoiceEndPoint(participantId, USERNAME, PASSWORD);
    }

    /**
     * Get the access point details if EHFV3 Invoice is enabled
     *
     * @param participantId
     * @param userName
     * @param password
     * @return the AccesspointDetails
     * @throws Exception
     */
    public static AccesspointDetails getEHFV3InvoiceEndPoint(String participantId, String userName, String password) throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        PostMethod httpPost = getHttpPostMethod(METHOD_GET_IF_PARTICIPANT_ENABLED_EHFV3_INVOICE);

        try {

            RequestEntity requestEntity
                    = new InputStreamRequestEntity(streamObject(participantId), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);

            int status = httpClient.executeMethod(httpPost);
            if (status == HttpStatus.SC_OK) {

                String endpointData = httpPost.getResponseBodyAsString();
                endpointData = endpointData.replace("{", "");
                endpointData = endpointData.replace("}", "");
                String[] endpointArray = endpointData.split(",");
                AccesspointDetails accesspointDetails = new AccesspointDetails();

                if (endpointArray.length >= 3) {

                    accesspointDetails.setUrl(endpointArray[0].split("=")[1]);
                    accesspointDetails.setBusDoxProtocol(endpointArray[1].split("=")[1]);
                    accesspointDetails.setCommonName(endpointArray[2].split("=")[1]);
                }
                return accesspointDetails;
            }
            throw new Exception(getTextMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }

    /**
     * Get the access point details if EHFV3 CreditNote is enabled
     *
     * @param participantId
     * @return the AccesspointDetails
     * @throws Exception
     */
    public static AccesspointDetails getEHFV3CreditNoteEndPoint(String participantId) throws Exception {
        return getEHFV3CreditNoteEndPoint(participantId, USERNAME, PASSWORD);
    }

    /**
     * Get the access point details if EHFV3 CreditNote is enabled
     *
     * @param participantId
     * @param userName
     * @param password
     * @return the AccesspointDetails
     * @throws Exception
     */
    public static AccesspointDetails getEHFV3CreditNoteEndPoint(String participantId, String userName, String password) throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        PostMethod httpPost = getHttpPostMethod(METHOD_GET_IF_PARTICIPANT_ENABLED_EHFV3_CREDITNOTE);

        try {

            RequestEntity requestEntity
                    = new InputStreamRequestEntity(streamObject(participantId), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);

            int status = httpClient.executeMethod(httpPost);
            if (status == HttpStatus.SC_OK) {

                String endpointData = httpPost.getResponseBodyAsString();
                endpointData = endpointData.replace("{", "");
                endpointData = endpointData.replace("}", "");
                String[] endpointArray = endpointData.split(",");
                AccesspointDetails accesspointDetails = new AccesspointDetails();

                if (endpointArray.length >= 3) {

                    accesspointDetails.setUrl(endpointArray[0].split("=")[1]);
                    accesspointDetails.setBusDoxProtocol(endpointArray[1].split("=")[1]);
                    accesspointDetails.setCommonName(endpointArray[2].split("=")[1]);
                }
                return accesspointDetails;
            }
            throw new Exception(getTextMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }

    /**
     * Get participant email.
     *
     * @param participantId the participant Id
     * @return the participant string[]
     * @throws Exception the exception
     */
    public static String[] getParticipantEmail(String participantId) throws Exception {
        return getParticipantEmail(participantId, USERNAME, PASSWORD);
    }

    /**
     * Creates the user.
     *
     * @param userCreationDTO the user creation dto
     * @return true, if successful
     * @throws Exception the exception
     */
    public static boolean createUser(UserDTO userCreationDTO) throws Exception {
        return createUser(userCreationDTO, USERNAME, PASSWORD);
    }

    /**
     * Creates the user.
     *
     * @param userCreationDTO the user creation dto
     * @param userName the user name
     * @param password the password
     * @return true, if successful
     * @throws Exception the exception
     */
    public static boolean createUser(UserDTO userCreationDTO, String userName,
            String password) throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        PostMethod httpPost = getHttpPostMethod(METHOD_CREATE_USER);

        try {

            RequestEntity requestEntity
                    = new InputStreamRequestEntity(streamObject(userCreationDTO), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);
            int status = httpClient.executeMethod(httpPost);

            if (status == HttpStatus.SC_OK) {
                return (Boolean) new ObjectInputStream(httpPost.getResponseBodyAsStream()).readObject();
            }
            throw new Exception(getTextMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }

    /**
     * Send order.
     *
     * @param documentDTO the document dto
     * @return the string
     * @throws Exception the exception
     */
    public static String sendOrder(DocumentDTO documentDTO) throws Exception {
        return sendOrder(documentDTO, USERNAME, PASSWORD);
    }

    /**
     * Send order.
     *
     * @param documentDTO the document dto
     * @param userName the user name
     * @param password the password
     * @return the string
     * @throws Exception the exception
     */
    public static String sendOrder(DocumentDTO documentDTO, String userName, String password)
            throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        PostMethod httpPost = getHttpPostMethod(METHOD_SEND_ORDER);

        try {

            RequestEntity requestEntity
                    = new InputStreamRequestEntity(streamObject(documentDTO), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);
            int status = httpClient.executeMethod(httpPost);

            if (status == HttpStatus.SC_OK) {
                return (String) new ObjectInputStream(httpPost.getResponseBodyAsStream()).readObject();
            }

            throw new Exception(getTextMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }

    public static String sendOrderResponse(DocumentDTO documentDTO) throws Exception {
        return sendOrderResponse(documentDTO, USERNAME, PASSWORD);
    }

    public static String sendOrderResponse(DocumentDTO documentDTO, String userName, String password)
            throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        PostMethod httpPost = getHttpPostMethod(METHOD_SEND_ORDER_RESPONSE);

        try {

            RequestEntity requestEntity
                    = new InputStreamRequestEntity(streamObject(documentDTO), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);
            int status = httpClient.executeMethod(httpPost);

            if (status == HttpStatus.SC_OK) {
                return (String) new ObjectInputStream(httpPost.getResponseBodyAsStream()).readObject();
            }

            throw new Exception(getTextMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }
    
    /**
     * Sending the document in Enhanced PEPPOL standards.
     *
     * @param documentDTO the document dto
     * @param userName the user name
     * @param password the password
     * @return message reference
     * @throws Exception the exception
     */
    public static String sendEPEPPOL(DocumentDTO documentDTO, String userName, String password)
            throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        PostMethod httpPost = getHttpPostMethod(METHOD_SEND_EPEPPOL);

        try {

            RequestEntity requestEntity
                    = new InputStreamRequestEntity(streamObject(documentDTO), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);
            int status = httpClient.executeMethod(httpPost);

            if (status == HttpStatus.SC_OK) {
                return (String) new ObjectInputStream(httpPost.getResponseBodyAsStream()).readObject();
            }

            throw new Exception(getTextMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }
    
    public static String sendEPEPPOL(DocumentDTO documentDTO) throws Exception {
        return sendEPEPPOL(documentDTO, USERNAME, PASSWORD);
    }
    
    /**
     * Get receipts 
     *
     * @param messageId
     * @param userName
     * @param password
     * @return
     * @throws Exception
     */
    public static List<ReceiptDTO> getEPEPPOLReceipt(String messageReference, String userName, String password) throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        GetMethod httpGet = getHttpGetMethod(METHOD_GET_EPEPPOL_RECEIPTS + "/" + messageReference);

        try {

            int status = httpClient.executeMethod(httpGet);

            if (status == HttpStatus.SC_OK) {
                return (List<ReceiptDTO>) new ObjectInputStream(httpGet.getResponseBodyAsStream()).readObject();
            }
            throw new Exception(getTextMessage(httpGet.getResponseBodyAsStream()));
        } finally {
            httpGet.releaseConnection();
        }
    }
    
    public static List<ReceiptDTO> getEPEPPOLReceipt(String messageReference) throws Exception {
        return getEPEPPOLReceipt(messageReference, USERNAME, PASSWORD);
    }
    
    
    /**
     * Get the recetly received receipt from the message reference
     *
     * @param messageId
     * @param userName
     * @param password
     * @return
     * @throws Exception
     */
    public static ReceiptDTO getLastEPEPPOLReceipt(String messageReference, String userName, String password) throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        GetMethod httpGet = getHttpGetMethod(METHOD_GET_EPEPPOL_LAST_RECEIPTS + "/" + messageReference);

        try {

            int status = httpClient.executeMethod(httpGet);

            if (status == HttpStatus.SC_OK) {
                return (ReceiptDTO) new ObjectInputStream(httpGet.getResponseBodyAsStream()).readObject();
            }
            throw new Exception(getTextMessage(httpGet.getResponseBodyAsStream()));
        } finally {
            httpGet.releaseConnection();
        }
    }    
    
    public static ReceiptDTO getLastEPEPPOLReceipt(String messageReference) throws Exception {
        return getLastEPEPPOLReceipt(messageReference, USERNAME, PASSWORD);
    }
    
    /**
     * Get the recetly received receipt from the message reference
     *
     * @param messageId
     * @param userName
     * @param password
     * @return
     * @throws Exception
     */
    public static List<ReceiptDTO> getAllNewReceipts(String userName, String password) throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        GetMethod httpGet = getHttpGetMethod(METHOD_GET_ALL_NEW_RECEIPTS);

        try {

            int status = httpClient.executeMethod(httpGet);

            if (status == HttpStatus.SC_OK) {
                return (List<ReceiptDTO>) new ObjectInputStream(httpGet.getResponseBodyAsStream()).readObject();
            }
            throw new Exception(getTextMessage(httpGet.getResponseBodyAsStream()));
        } finally {
            httpGet.releaseConnection();
        }
    }    
    
    public static List<ReceiptDTO> getAllNewReceipts() throws Exception {
        return getAllNewReceipts( USERNAME, PASSWORD);
    }
    
    /**
     * Mark receipt as read.
     *
     * @param receiptIds the receipt ids
     * @param userName the user name
     * @param password the password
     * @return true, if successful
     * @throws Exception the exception
     */
    public static boolean markReceiptAsRead(List<String> receiptIds, String userName, String password)
            throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        PostMethod httpPost = getHttpPostMethod(METHOD_MARK_RECEIPTS);

        try {

            RequestEntity requestEntity
                    = new InputStreamRequestEntity(streamObject(receiptIds), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);
            int status = httpClient.executeMethod(httpPost);

            if (status == HttpStatus.SC_OK) {
                return (Boolean) new ObjectInputStream(httpPost.getResponseBodyAsStream()).readObject();
            }
            throw new Exception(getTextMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }

    /**
     * Mark receipt as read.
     *
     * @param receiptIds the receipt ids
     * @return true, if successful
     * @throws Exception the exception
     */
    public static boolean markReceiptAsRead(List<String> receiptIds) throws Exception {
        return markReceiptAsRead(receiptIds, USERNAME, PASSWORD);
    }


    /**
     * Get un Read Message Id for Web
     *
     * @param syncDate
     * @param userName
     * @param password
     * @return
     * @throws Exception
     */
    public static MessageIdListDTO getUnReadMessageIdOfOrder(String userName, String password, String participentId)
            throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        PostMethod httpPost = getHttpPostMethod(METHOD_RECEIVE_UN_READ_MESSAGE_ID_OF_ORDER + "/");

        try {

            RequestEntity requestEntity
                    = new InputStreamRequestEntity(streamObject(participentId), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);

            int status = httpClient.executeMethod(httpPost);

            if (status == HttpStatus.SC_OK) {
                return (MessageIdListDTO) new ObjectInputStream(httpPost.getResponseBodyAsStream()).readObject();
            }
            throw new Exception(getTextMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }

      /**
     * Get un Read Message Id for Web
     *
     * @param participentId receiver participent id if null returns messages of all receiver
     *
     * @return MessageIdListDTO
     * @throws Exception
     */
    public static MessageIdListDTO getUnReadMessageIdOfOrder(String participentId) throws Exception {
        return getUnReadMessageIdOfOrder(USERNAME, PASSWORD, participentId);
    }

         /**
     * Get un Read Message Id for Web
     *
     * @param syncDate
     * @param userName
     * @param password
     * @return
     * @throws Exception
     */
    public static MessageIdListDTO getUnReadMessageIdOfOrderResponse(String userName, String password, String participentId)
            throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        PostMethod httpPost = getHttpPostMethod(METHOD_RECEIVE_UN_READ_MESSAGE_ID_OF_ORDER_RESPONSE + "/");

        try {

            RequestEntity requestEntity
                    = new InputStreamRequestEntity(streamObject(participentId), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);

            int status = httpClient.executeMethod(httpPost);

            if (status == HttpStatus.SC_OK) {
                return (MessageIdListDTO) new ObjectInputStream(httpPost.getResponseBodyAsStream()).readObject();
            }
            throw new Exception(getTextMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }

    /**
     * Get un Read Message Id for Web
     *
     * @param participentId receiver participent id if null returns messages of all receiver
     *
     * @return MessageIdListDTO
     * @throws Exception
     */
    public static MessageIdListDTO getUnReadMessageIdOfOrderResponse(String participentId) throws Exception {
        return getUnReadMessageIdOfOrderResponse(USERNAME, PASSWORD, participentId);
    }

       /**
     * Get the access point details if EHFV3 Order is enabled
     *
     * @param participantId
     * @param userName
     * @param password
     * @return
     * @throws Exception
     */
    public static AccesspointDetails getEHFV3OrderEndPoint(String participantId, String userName, String password) throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        PostMethod httpPost = getHttpPostMethod(METHOD_GET_IF_PARTICIPANT_ENABLED_EHFV3_ORDER);

        try {

            RequestEntity requestEntity
                    = new InputStreamRequestEntity(streamObject(participantId), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);

            int status = httpClient.executeMethod(httpPost);
            if (status == HttpStatus.SC_OK) {

                String endpointData = httpPost.getResponseBodyAsString();
                endpointData = endpointData.replace("{", "");
                endpointData = endpointData.replace("}", "");
                String[] endpointArray = endpointData.split(",");
                AccesspointDetails accesspointDetails = new AccesspointDetails();

                if (endpointArray.length >= 3) {

                    accesspointDetails.setUrl(endpointArray[0].split("=")[1]);
                    accesspointDetails.setBusDoxProtocol(endpointArray[1].split("=")[1]);
                    accesspointDetails.setCommonName(endpointArray[2].split("=")[1]);
                }
                return accesspointDetails;
            }
            throw new Exception(getTextMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }

    /**
     * Get the access point details if EHFV3 order response is enabled
     *
     * @param participantId
     * @param userName
     * @param password
     * @return the AccesspointDetails
     * @throws Exception
     */
    public static AccesspointDetails getEHFV3OrderResponseEndPoint(String participantId, String userName, String password) throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        PostMethod httpPost = getHttpPostMethod(METHOD_GET_IF_PARTICIPANT_ENABLED_EHFV3_ORDER_RESPONSE);

        try {

            RequestEntity requestEntity
                    = new InputStreamRequestEntity(streamObject(participantId), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);

            int status = httpClient.executeMethod(httpPost);
            if (status == HttpStatus.SC_OK) {

                String endpointData = httpPost.getResponseBodyAsString();
                endpointData = endpointData.replace("{", "");
                endpointData = endpointData.replace("}", "");
                String[] endpointArray = endpointData.split(",");
                AccesspointDetails accesspointDetails = new AccesspointDetails();

                if (endpointArray.length >= 3) {

                    accesspointDetails.setUrl(endpointArray[0].split("=")[1]);
                    accesspointDetails.setBusDoxProtocol(endpointArray[1].split("=")[1]);
                    accesspointDetails.setCommonName(endpointArray[2].split("=")[1]);
                }
                return accesspointDetails;
            }
            throw new Exception(getTextMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }


    /**
     * Send order.
     *
     * @param documentDTO the document dto
     * @return the string
     * @throws Exception the exception
     */
    public static String sendEHFV3Order(DocumentDTO documentDTO) throws Exception {
        return sendEHFV3Order(documentDTO, USERNAME, PASSWORD);
    }

    /**
     * Send order.
     *
     * @param documentDTO the document dto
     * @param userName the user name
     * @param password the password
     * @return the string
     * @throws Exception the exception
     */
    public static String sendEHFV3Order(DocumentDTO documentDTO, String userName, String password)
            throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        PostMethod httpPost = getHttpPostMethod(METHOD_SEND_EHFV3_ORDER);

        try {

            RequestEntity requestEntity
                    = new InputStreamRequestEntity(streamObject(documentDTO), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);
            int status = httpClient.executeMethod(httpPost);

            if (status == HttpStatus.SC_OK) {
                return (String) new ObjectInputStream(httpPost.getResponseBodyAsStream()).readObject();
            }

            throw new Exception(getTextMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }

    public static String sendEHFV3OrderResponse(DocumentDTO documentDTO) throws Exception {
        return sendEHFV3OrderResponse(documentDTO, USERNAME, PASSWORD);
    }

    public static String sendEHFV3OrderResponse(DocumentDTO documentDTO, String userName, String password)
            throws Exception {

        HttpClient httpClient = getHttpClient(userName, password);
        PostMethod httpPost = getHttpPostMethod(METHOD_SEND_EHFV3_ORDER_RESPONSE);

        try {

            RequestEntity requestEntity
                    = new InputStreamRequestEntity(streamObject(documentDTO), CONTENT_TYPE);
            httpPost.setRequestEntity(requestEntity);
            int status = httpClient.executeMethod(httpPost);

            if (status == HttpStatus.SC_OK) {
                return (String) new ObjectInputStream(httpPost.getResponseBodyAsStream()).readObject();
            }

            throw new Exception(getTextMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }


}
