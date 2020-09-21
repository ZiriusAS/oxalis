/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.difi.oxalis.service.transmission;

import ehandel.no.EHFConstants;
import ehandel.no.util.StringUtils;
import eu.peppol.outbound.api.DocumentDTO;
import eu.peppol.outbound.api.EmailDTO;
import eu.peppol.outbound.api.MessageDTO;
import eu.peppol.outbound.api.MessageIdListDTO;
import eu.peppol.outbound.api.MessageListDTO;
import eu.peppol.outbound.api.MessageRemoverDTO;
import eu.peppol.outbound.api.UserDTO;
import eu.peppol.outbound.api.UserRole;
import eu.peppol.outbound.client.EHFSchemaValidator;
import eu.sendregning.oxalis.CustomMain;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.xml.ws.http.HTTPException;
import no.difi.oxalis.api.lang.OxalisException;
import no.difi.oxalis.api.lookup.LookupService;
import no.difi.oxalis.api.outbound.TransmissionResponse;
import no.difi.oxalis.outbound.OxalisOutboundComponent;
import no.difi.oxalis.service.bo.AuditLogBO;
import no.difi.oxalis.service.bo.OutboundBO;
import no.difi.oxalis.service.model.AuditEvent;
import no.difi.oxalis.service.model.AuditLog;
import no.difi.oxalis.service.model.MessageInfo;
import eu.peppol.outbound.api.ReceiptDTO;
import no.difi.oxalis.api.lang.EvidenceException;
import no.difi.oxalis.service.util.C2ReceiptGenerator;
import no.difi.oxalis.service.util.OutboundConstants;
import no.difi.oxalis.service.util.Property;
import no.difi.oxalis.service.util.PropertyUtil;
import no.difi.oxalis.sniffer.PeppolStandardBusinessHeader;
import no.difi.oxalis.sniffer.identifier.PeppolDocumentTypeId;
import no.difi.vefa.peppol.common.model.DocumentTypeIdentifier;
import no.difi.vefa.peppol.common.model.Endpoint;
import no.difi.vefa.peppol.common.model.Header;
import no.difi.vefa.peppol.common.model.ParticipantIdentifier;
import no.difi.vefa.peppol.common.model.ProcessIdentifier;
import no.difi.vefa.peppol.security.lang.PeppolSecurityException;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aktharhussainis
 */
public class OutboundService extends BaseService{

    private static Boolean testEnvironment = null;
    private static Boolean overrideHeader = null;
    private static String oxalisServerUrl = null;
    private static String oxalisCertificatePath = null;
    private static String outboundMsgDir;
    private static String followUpMsgDir;
    private static final String XML_EXT = "xml";
    private static final String[] EXT_ARR = {XML_EXT};
    private static String evidencePath = "";
    private static String validatorURL = "";
    
    private static String DS_NAME="";
    
    private static OutboundService instance;
    private static OxalisOutboundComponent oxalisOutboundComponent = null;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(OutboundService.class);

    private String ePEPPOLReceiptPath = "";
    private C2ReceiptGenerator c2ReceiptGenerator = null;

    protected OutboundService() {
        
    }

    public static OutboundService getInstance() {

        if (instance == null) {
            synchronized (OutboundService.class) {
                return createSingleton();
            }
        }
        return instance;
    }

    private static OutboundService createSingleton() {

        if (instance != null) {
            return instance;
        }
        instance = new OutboundService();
        instance.initialize();
        return instance;
    }
    
      private static OxalisOutboundComponent getOutBoundComponent() {

        if (oxalisOutboundComponent == null) {
            oxalisOutboundComponent = new OxalisOutboundComponent();
        }
        return oxalisOutboundComponent;
    }

    /**
     * Initialize the properties.
     */
    private void initialize() {

        try {
            
            testEnvironment = Boolean.parseBoolean(PropertyUtil.getProperty(Property.TEST_ENVIRONMENT));
            //overrideHeader = Boolean.parseBoolean(globalConfiguration.getOverrideHeader());
            oxalisServerUrl = PropertyUtil.getProperty(Property.OXALIS_SERVER_URL);
            oxalisCertificatePath = PropertyUtil.getProperty(Property.OXALIS_CERTIFICATE_PATH);
            outboundMsgDir =  PropertyUtil.getProperty(Property.OUTBOUND_MESSAGE_STORE_PATH);
            followUpMsgDir =  PropertyUtil.getProperty(Property.FOLLOWUP_MESSAGE_STORE_PATH);
            DS_NAME = PropertyUtil.getProperty(Property.DATASOURCE_NAME);
            
            evidencePath = PropertyUtil.getProperty(Property.EVIDENCE_PATH);
            validatorURL = PropertyUtil.getProperty(Property.VALIDATOR_URL);
            
            ePEPPOLReceiptPath = PropertyUtil.getProperty(Property.EPEPPOL_RECEIPT_PATH);
            
            c2ReceiptGenerator = C2ReceiptGenerator.getInstance();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
     public String sendDocument(DocumentDTO documentDTO, String userId, boolean isResendDocument) throws IOException,
            ClassNotFoundException, Exception {
         
         // perform a validation 
         EHFSchemaValidator.setClientUrl(validatorURL);
         
         String validatorResult = EHFSchemaValidator.validateEhfXml(IOUtils.toString(documentDTO.getFileData(), CharEncoding.UTF_8));
         boolean valid = true;
         try {
            
             valid = isValidEHF(validatorResult);
         } catch (Exception ex) {
             throw new RuntimeException("Unable to validate Document",ex);
         }
         
         if(!valid) { 
             throw new RuntimeException("Invalid Document");
         }
         
        File testFile = null;
        eu.sendregning.oxalis.CustomMain obj = CustomMain.getInstance(testEnvironment, oxalisServerUrl, oxalisCertificatePath);
        
        String transmissionIdentifier = null;

        try {
            
            String documentType = identifyDocumentTypeFromContent(documentDTO.getFileData());
            PeppolStandardBusinessHeader sbdh = obj.createSBDH(documentDTO.getSenderId(), documentDTO.getReceiverId(), documentType, EHFConstants.EHF_THREE_DOT_ZERO_PROFILE_ID.getValue());
            byte[] contentWrapedWithSbdh = obj.wrapPayLoadWithSBDH(documentDTO.getFileData(), sbdh);
            File evidence = null;

            if(testEnvironment) {

                LOGGER.info(" ##### Sending Document : TEST #####");
                testFile = File.createTempFile(UUID.randomUUID().toString(), ".xml");
                try (FileOutputStream fos = new FileOutputStream(testFile)) {
                    
                    fos.write(contentWrapedWithSbdh);
                    LOGGER.info(" --- Temp File generated for Testing " + testFile.getName());
                }
                TransmissionResponse transmissionReponse = obj.send(testFile.getPath());
                
                transmissionIdentifier = transmissionReponse.getTransmissionIdentifier().getIdentifier();
                evidence = File.createTempFile(transmissionIdentifier, ".receipt.dat");
                
                try (FileOutputStream outputStream = new FileOutputStream(evidence)) {
                    
                    getOutBoundComponent().getEvidenceFactory().write(outputStream, transmissionReponse);
                }
                
                // move to actual location
                evidence.renameTo(new File(evidencePath + File.separator + evidence.getName()));
            } else {
                
                try(InputStream inputStream = new ByteArrayInputStream(contentWrapedWithSbdh)) {

                    LOGGER.info(" ##### Sending Document : PRODUCTION #####");
                    TransmissionResponse transmissionReponse = obj.sendDocumentUsingFactory(inputStream);
                    transmissionIdentifier = transmissionReponse.getTransmissionIdentifier().getIdentifier();
                    
                    evidence = File.createTempFile(transmissionIdentifier, ".receipt.dat");
                    
                
                    try (FileOutputStream outputStream = new FileOutputStream(evidence)) {

                        getOutBoundComponent().getEvidenceFactory().write(outputStream, transmissionReponse);
                    }
                    
                    // move to actual location
                    evidence.renameTo(new File(evidencePath + File.separator + evidence.getName()));
                }
            }

            LOGGER.info(String.format(" Send Document : SUCCESS \n Transmission Id : %s \n Sender : %s \n Receiver : %s", transmissionIdentifier, documentDTO.getSenderId(), documentDTO.getReceiverId()));
            putAuditLog(transmissionIdentifier, documentDTO, userId, isResendDocument, evidence.getName());

        } catch(HTTPException | OxalisException | NoSuchAlgorithmException | PeppolSecurityException e) {

            LOGGER.error(String.format(" Send Document : FAIL  \n Sender : %s \n Receiver : %s", documentDTO.getSenderId(), documentDTO.getReceiverId()), e);
            putAuditLog(null, documentDTO, userId, isResendDocument, e.getLocalizedMessage());

            if (!isResendDocument) {
                saveFileInOutBox(documentDTO);
            }
        } catch(Exception e) {
            LOGGER.error(" *** Exception : Unable to send Document *** " , e);
            throw e;
        } finally {

            if(testEnvironment) {
                if (testFile != null && testFile.exists()) {
                    testFile.delete();
                }
            }
        }
        return transmissionIdentifier;
    }
     
    public String sendEPEPPOLDocument(DocumentDTO documentDTO, String userId) throws IOException,
            ClassNotFoundException, Exception {
         
        File testFile = null;
        eu.sendregning.oxalis.CustomMain obj = CustomMain.getInstance(testEnvironment, oxalisServerUrl, oxalisCertificatePath);
        
        String transmissionIdentifier = null;
        byte[] contentWrapedWithSbdh = null;

        try {
             
            contentWrapedWithSbdh = wrapContentWithHeader(documentDTO,obj);
            
            if(testEnvironment) {

                LOGGER.info(" ##### Sending Document : TEST #####");
                testFile = File.createTempFile(UUID.randomUUID().toString(), ".xml");
                try (FileOutputStream fos = new FileOutputStream(testFile)) {
                    
                    fos.write(contentWrapedWithSbdh);
                    LOGGER.info(" --- Temp File generated for Testing " + testFile.getName());
                }
                
                // generate c2 receipt acknowledgement
                c2ReceiptGenerator.generateAcknowledgementFromSDB(contentWrapedWithSbdh);
                
                TransmissionResponse transmissionReponse = obj.send(testFile.getPath());
                
                transmissionIdentifier = transmissionReponse.getTransmissionIdentifier().getIdentifier();
                
                storeEvidenceOnEPEPPOLPath(transmissionIdentifier,transmissionReponse);
            } else {
                
                try(InputStream inputStream = new ByteArrayInputStream(contentWrapedWithSbdh)) {
                    
                    // generate c2 receipt acknowledgement
                    c2ReceiptGenerator.generateAcknowledgementFromSDB(contentWrapedWithSbdh);
                    
                    LOGGER.info(" ##### Sending Document : PRODUCTION #####");
                    TransmissionResponse transmissionReponse = obj.sendDocumentUsingFactory(inputStream);
                    transmissionIdentifier = transmissionReponse.getTransmissionIdentifier().getIdentifier();
                    
                    storeEvidenceOnEPEPPOLPath(transmissionIdentifier,transmissionReponse);
                }
            }
                        
            LOGGER.info(String.format(" Send Document : SUCCESS \n Transmission Id : %s \n Sender : %s \n Receiver : %s", transmissionIdentifier, documentDTO.getSenderId(), documentDTO.getReceiverId()));
            putAuditLog(transmissionIdentifier, documentDTO, userId, false, transmissionIdentifier);

        } catch(HTTPException | OxalisException | NoSuchAlgorithmException | PeppolSecurityException e) {
            
            // generate c2 receipt exception
            c2ReceiptGenerator.generateExceptionFromSDB(contentWrapedWithSbdh, e);
            
            LOGGER.error(String.format(" Send Document : FAIL  \n Sender : %s \n Receiver : %s", documentDTO.getSenderId(), documentDTO.getReceiverId()), e);
            putAuditLog(null, documentDTO, userId, false, e.getLocalizedMessage());

        } catch(Exception e) {
            
            // generate c2 receipt exception
            c2ReceiptGenerator.generateExceptionFromSDB(contentWrapedWithSbdh, e);
            
            LOGGER.error(" *** Exception : Unable to send Document *** " , e);
            throw e;
        } finally {

            if(testEnvironment) {
                if (testFile != null && testFile.exists()) {
                    testFile.delete();
                }
            }
        }
        return transmissionIdentifier;
    }
    
    
    private byte[] wrapContentWithHeader(DocumentDTO documentDTO, eu.sendregning.oxalis.CustomMain obj) throws Exception {
        if (documentDTO.isIsSDB()) {

            String documentType = identifyDocumentTypeFromContent(documentDTO.getFileData());
            PeppolStandardBusinessHeader sbdh = obj.createSBDH(documentDTO.getSenderId(), documentDTO.getReceiverId(), documentType, EHFConstants.EHF_THREE_DOT_ZERO_PROFILE_ID.getValue());
            return obj.wrapPayLoadWithSBDH(documentDTO.getFileData(), sbdh);
        } else {

            return documentDTO.getFileData();
        }
    }
    
    private void storeEvidenceOnEPEPPOLPath(String name, no.difi.oxalis.api.outbound.TransmissionResponse transmissionReponse) throws IOException, EvidenceException {
        
        File evidence = evidence = File.createTempFile(name, ".receipt.dat");

        try (FileOutputStream outputStream = new FileOutputStream(evidence)) {

            getOutBoundComponent().getEvidenceFactory().write(outputStream, transmissionReponse);
        }

        // move to epeppol receipt location
        evidence.renameTo(new File(ePEPPOLReceiptPath + File.separator + evidence.getName()));
        
    }
     
    private boolean isValidEHF(String jsonString) {

        try {

            JSONArray result = new JSONArray(jsonString.trim());
            if (result != null) {

                for (int i = 0; i < result.length(); i++) {
                    JSONObject section = (JSONObject) result.get(i);
                    if (section.get("flag") != null
                            && (section.get("flag").toString().toUpperCase().contains("ERROR")
                                    || section.get("flag").toString().toUpperCase().contains("FATAL"))) {
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    } 
     
    private String identifyDocumentTypeFromContent(byte[] documentContent) {
        
        
        try {

            String invoiceFile = IOUtils.toString(documentContent, CharEncoding.UTF_8);
            if (invoiceFile.contains("CreditNote>")) {
                return EHFConstants.EHF_THREE_DOT_ZERO_CREDIT_NOTE.getValue();
            }

            return EHFConstants.EHF_THREE_DOT_ZERO_INVOICE.getValue();
        } catch (Exception e) {
            LOGGER.warn(" !!!! Unable to file Document type.Hence considering this as Invoice ");
            return EHFConstants.EHF_THREE_DOT_ZERO_INVOICE.getValue();
        }
    }

    private void putAuditLog(String transmissionIdentifier, DocumentDTO documentDTO, String userId, boolean isResendDocument, String exceptionMsg) {

        try {

            LOGGER.info(" Creating Audit Log :  " + transmissionIdentifier);
            AuditLogBO auditBO = new AuditLogBO(DS_NAME);
            auditBO.createAuditLogEntry(prepareAuditInfo(transmissionIdentifier, documentDTO, userId, isResendDocument, exceptionMsg));
        } catch (Exception e) {
            LOGGER.error((" Unable to create audit log : " + transmissionIdentifier), e);
        }
    }
     
     private void saveFileInOutBox(DocumentDTO documentDTO) throws IOException {

        try {

            if (new File(this.outboundMsgDir).exists()) {
                
                String fileName = String.format("%s_%s_%s.%s", documentDTO.getSenderId().replace(":", "-"), documentDTO.getReceiverId().replace(":", "-"), UUID.randomUUID().toString(), XML_EXT);  // Ex: 0192-986920080_0192-986920080_e880a1de-345d-492e-bcf4-a0a0a800d011.xml

                File file = new File(String.format("%s/%s", outboundMsgDir, fileName));
                
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    
                    fos.write(documentDTO.getFileData());
                    LOGGER.info(" --- Transmission saved in OutBox for Retry : " + fileName);
                }
            }
        } catch (IOException e) {
            LOGGER.error(" Unable to save document in outbox", e);
            throw e;
        }
     }
    
    /**
     * Get Message Info
     * 
     * @param messageId
     * @param userId
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws FaultMessage 
     */
     public MessageDTO getMessageInfo(String messageId, String userId) throws IOException,
            ClassNotFoundException, Exception {

        if (StringUtils.isEmpty(messageId)) {
            throw new ServerException(OutboundConstants.INVALID_DATA);
        }

        OutboundBO outboundBO = new OutboundBO(DS_NAME);
        MessageInfo messageInfo = outboundBO.getMessage(messageId);
        MessageDTO messageDTO = getMessageDTO(messageInfo);
        
        return messageDTO;
    }
     
    /**
     * Get Message Info
     * 
     * @param messageId
     * @param userId
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws FaultMessage 
     */
     public List<ReceiptDTO> getEPEPPOLReceipts(String messageReference, boolean recentOnly) throws IOException,
            ClassNotFoundException, Exception {

        OutboundBO outboundBO = new OutboundBO(DS_NAME);
        List<ReceiptDTO> receiptInfos = outboundBO.getReceipts(messageReference,recentOnly);
        
        return receiptInfos;
    }
     
    /**
      * Get Message Id
      * 
      * @param participantId
      * @param userId
      * @return
      * @throws IOException
      * @throws ClassNotFoundException
      * @throws FaultMessage 
      */
    public MessageIdListDTO getMessageIds(String participantId, String userId) throws IOException,
            ClassNotFoundException, Exception {

        if (StringUtils.isEmpty(participantId)) {
            throw new ServerException(OutboundConstants.INVALID_DATA);
        }

        OutboundBO outboundBO = new OutboundBO(DS_NAME);
        List<String> messageIdList = outboundBO.getMessageIds(participantId, false);

        MessageIdListDTO messageIdListDTO  = new MessageIdListDTO();
        messageIdListDTO.setMessageIdList(messageIdList);
        
        return messageIdListDTO;
    }
    
    public MessageIdListDTO getMessageIds(Date syncDate, String userId) throws IOException,
            ClassNotFoundException, Exception {
        
        List<String> messageIdList = new ArrayList<String>();
        MessageIdListDTO messageIdListDTO = new MessageIdListDTO();
        OutboundBO outboundBO = new OutboundBO(DS_NAME);

        if (syncDate != null) { 

            messageIdList = outboundBO.getMessageIds(new java.sql.Timestamp(syncDate.getTime()));
        } else {

            Date now = Calendar.getInstance().getTime();
            messageIdList = outboundBO.getMessageIds(new java.sql.Timestamp(now.getTime()));
        }

        messageIdListDTO.setMessageIdList(messageIdList);

        return messageIdListDTO;
    }
    
    /**
     * Get All Message Id
     * 
     * @param timestamp
     * @param userId
     * @return
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public MessageIdListDTO getAllUnReadMessageIdForWeb(String userId) throws IOException,
            ClassNotFoundException, Exception {
        
        List<String> messageIdList = new ArrayList<String>();
        MessageIdListDTO messageIdListDTO = new MessageIdListDTO();
        OutboundBO outboundBO = new OutboundBO(DS_NAME);
        
        try {

            messageIdList = outboundBO.getAllUnReadMessageIdForWeb();
            messageIdListDTO.setMessageIdList(messageIdList);
        } catch (Exception e) {
            
        }
        
        return messageIdListDTO;
    }
    
    /**
     * Mark a participant's message as read.
     * 
     * @param messageIds
     *            the message ids
     * @param userId
     *            the user id
     * @return true, if successful
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws ClassNotFoundException
     *             the class not found exception
     * @throws FaultMessage
     *             the fault message
     */
    public boolean markAsRead(List<String> messageIds, String userId) throws IOException,
            ClassNotFoundException, Exception {
        
        OutboundBO outboundBO = new OutboundBO(DS_NAME);
        boolean result = false;

        if (messageIds == null || messageIds.isEmpty()) {
            throw new ServerException(OutboundConstants.INVALID_DATA);
        }

        result = outboundBO.markAsRead(messageIds);
        return result;
    }
    
    /**
     * Mark a participant's message as read from web.
     * 
     * @param messageIds
     *            the message ids
     * @param userId
     *            the user id
     * @return true, if successful
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws ClassNotFoundException
     *             the class not found exception
     * @throws FaultMessage
     *             the fault message
     */
    public boolean markAsReadFromWeb(List<String> messageIds, String userId) throws IOException,
            ClassNotFoundException, Exception {
        
        OutboundBO outboundBO = new OutboundBO(DS_NAME);
        boolean result = false;

        if (messageIds == null || messageIds.isEmpty()) {
            throw new ServerException(OutboundConstants.INVALID_DATA);
        }

        result = outboundBO.markAsReadFromWeb(messageIds);
        return result;
    }
    
    
        /**
     * Mark a participant's message as read from web.
     * 
     * @param messageIds
     *            the message ids
     * @param userId
     *            the user id
     * @return true, if successful
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws ClassNotFoundException
     *             the class not found exception
     * @throws FaultMessage
     *             the fault message
     */
    public boolean markReceiptAsRead(List<String> messageIds, String userId) throws IOException,
            ClassNotFoundException, Exception {
        
        OutboundBO outboundBO = new OutboundBO(DS_NAME);
        boolean result = false;

        if (messageIds == null || messageIds.isEmpty()) {
            throw new ServerException(OutboundConstants.INVALID_DATA);
        }

        result = outboundBO.markReceiptAsRead(messageIds);
        return result;
    }
    
    public Endpoint getAccesspointDetails(String participantId, String userId) {
        
        OxalisOutboundComponent oxalisOutboundComponent = getOutBoundComponent();
        LookupService lookupService = oxalisOutboundComponent.getLookupService();
        Endpoint endpoint = null;
        
        try {
            Header header = Header.newInstance()
                .receiver(ParticipantIdentifier.of(participantId))
                .documentType(PeppolDocumentTypeId.valueOf(EHFConstants.EHF_THREE_DOT_ZERO_INVOICE.getValue()).toVefa())
                .process(ProcessIdentifier.of(EHFConstants.EHF_THREE_DOT_ZERO_PROFILE_ID.getValue()));

            endpoint = lookupService.lookup(header);
            return endpoint;
        } catch (Exception e) {
            LOGGER.warn(" Unable to get Accesspoint Details " , e);
        }

        return null;
    }
    
     /**
      * Get Endpoint if EHFV3 Invoice enabled
      * 
      * @param participantId
      * @param userId
      * @return Endpoint 
      * @throws IOException
      * @throws ClassNotFoundException
      * @throws FaultMessage 
      */
    public Endpoint getEHFV3InvoiceEndPoint(String participantId, String userId) throws IOException,
            ClassNotFoundException, Exception {
        
        OxalisOutboundComponent oxalisOutboundComponent = getOutBoundComponent();
        LookupService lookupService = oxalisOutboundComponent.getLookupService();
        Endpoint endpoint = null;       
  
        try {
            Header header = Header.newInstance()
                .receiver(ParticipantIdentifier.of(participantId))
                .documentType(PeppolDocumentTypeId.valueOf(EHFConstants.EHF_THREE_DOT_ZERO_INVOICE.getValue()).toVefa())
                .process(ProcessIdentifier.of(EHFConstants.EHF_THREE_DOT_ZERO_PROFILE_ID.getValue()));

            endpoint = lookupService.lookup(header);
        } catch (Exception e) {
            LOGGER.error("Exception in getEHFV3InvoiceEndPoint ", e);
            throw e;
        }   

        return endpoint;
    }
     /**
      * Get Endpoint if EHFV3 CreditNote enabled
      * 
      * @param participantId
      * @param userId
      * @return Endpoint
      * @throws IOException
      * @throws ClassNotFoundException
      * @throws FaultMessage 
      */
    public Endpoint getEHFV3CreditNoteEndPoint(String participantId, String userId) throws IOException,
            ClassNotFoundException, Exception {
        
        OxalisOutboundComponent oxalisOutboundComponent = getOutBoundComponent();
        LookupService lookupService = oxalisOutboundComponent.getLookupService();
        Endpoint endpoint = null;       
  
        try {
            Header header = Header.newInstance()
                .receiver(ParticipantIdentifier.of(participantId))
                .documentType(PeppolDocumentTypeId.valueOf(EHFConstants.EHF_THREE_DOT_ZERO_CREDIT_NOTE.getValue()).toVefa())
                .process(ProcessIdentifier.of(EHFConstants.EHF_THREE_DOT_ZERO_PROFILE_ID.getValue()));

            endpoint = lookupService.lookup(header);
            
        } catch (Exception e) {
            LOGGER.error("Exception in getEHFV3InvoiceEndPoint ", e);
            throw e;
        }
        return endpoint;
    }
    
/*    public MessageListDTO getAllMessages(Date syncDate, String userId) throws IOException,
            ClassNotFoundException {

        Log.info("Receiving files after " + syncDate);

        List<MessageInfo> messageInfoList = new ArrayList<MessageInfo>();                    
        MessageListDTO messageListDTO = new MessageListDTO();
        OutboundBO outboundBO = new OutboundBO(DS_NAME);  
        
        try {

            if (syncDate != null) { 

                messageInfoList = outboundBO.getAllMessages(new java.sql.Timestamp(syncDate.getTime()));
            } else {

                Date now = Calendar.getInstance().getTime();
                messageInfoList = outboundBO.getAllMessages(new java.sql.Timestamp(now.getTime()));
            }
        
            Mapper mapper = new DozerBeanMapper();
            List<MessageDTO> msgListDTO = null;
            
            for (MessageInfo messageInfo : messageInfoList) {
                msgListDTO.add(mapper.map(messageInfo, MessageDTO.class));
            }

            messageListDTO.setMessages(msgListDTO);

        } catch (Exception e) {
            System.err.println(e);
        }

        return messageListDTO;
    }
    
    public boolean getAccessPointLastSyncDateTime() throws IOException, ClassNotFoundException,
        NamingException {

        OutboundBO outboundBO = null;

        try {
            outboundBO = new OutboundBO(DS_NAME);
            Log.debug("Database Name: "+ DS_NAME);
            Date lastSyncDateTime = outboundBO.getAccessPointLastSyncDateTime(ACCESSPOINT_ID);
            System.out.println("lastSyncDateTime: " + lastSyncDateTime);

        } catch (Throwable e) {
            Log.error("Unable to post message count", e);
        } finally {
            outboundBO.cleanup();
        }
        return true;
    }*/

    /**
     * Get File content
     * 
     * @param fileName
     * @return file content
     * @throws Exception
     */
    public byte[] getFileContentByFileName(String fileName) {

        if (fileName == null || fileName.isEmpty()) {
            throw new ServerException(OutboundConstants.INVALID_DATA);
        }
        //return ObjectStorage.getFile(fileName);
        return null;
    }

     /**
     * resend message
     * @throws Exception
     */
     public void resend() {

        File dir = new File(outboundMsgDir);
        List<File> files;
        files = (List<File>) FileUtils.listFiles(dir, EXT_ARR, true);

        for (File file : files) {

            String transmissionID = null;
            LOGGER.info(" Resend Document : " + file.getName());
            try (FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath())) {

                DocumentDTO documentDTO = new DocumentDTO();
                documentDTO.setFileData(IOUtils.toByteArray(fileInputStream));
                documentDTO.setLicenseId("PREPAID");
                documentDTO.setFileName(file.getName());

                String[] fileNameParts = file.getName().split("_");

                if (fileNameParts != null && fileNameParts.length > 2) {

                    String senderId = fileNameParts[0].replace("-", ":"); // replace - from sender Ex : 0192-986920080 to 0192:986920080
                    String reciverId = fileNameParts[1].replace("-", ":"); // replace - from reciver Ex : 0192-986920080 to 0192:986920080

                    documentDTO.setSenderId(senderId);
                    documentDTO.setReceiverId(reciverId);

                    transmissionID = sendDocument(documentDTO, DS_NAME, true);

                    if (transmissionID != null) {                        
                        LOGGER.info(" Resend : SUCCESS : " + file.getName() + " ==> " + transmissionID);
                    } else {
                        LOGGER.info(" Resend : FAIL : " + file.getName() + " ==> " + transmissionID);
                    }
                } else {
                    LOGGER.info(" Resend : SKIPPED : " + file.getName());
                }
            } catch (Exception ex) {
                if (file != null) {
                    LOGGER.warn(" Unable to resend file : " + file.getName(), ex);
                } 
            } finally {

                if (transmissionID != null) {                    
                    file.delete();
                } else {
                    if (!canRetry(file)) {
                        moveFileToFollowup(file);
                    }                    
                }
            }
        }
    }
     
     private void moveFileToFollowup(File outBoundFile) {

        try {

            if (outBoundFile.exists()) {
                outBoundFile.renameTo(new File(String.format("%s/%s", followUpMsgDir, outBoundFile.getName())));
                LOGGER.info(" File moved to followup : " + outBoundFile.getName() );
            }
        } catch (Exception e) {
           LOGGER.warn(" Problem in moving file to followup " , e);
        }
     }

    private boolean canRetry(File outBoundFile) {

        try {
            
            BasicFileAttributes attr = Files.readAttributes(outBoundFile.toPath(), BasicFileAttributes.class);
            final Instant toInstant = attr.creationTime().toInstant();
            long hours = Duration.between(toInstant, Instant.now()).toHours();

            if (hours >= 24) {

                LOGGER.info(" Maximum retry attemt reached : " + outBoundFile.getName());
                return false;
            }

            return true;
        } catch (Exception e) {
            LOGGER.warn((" Problem in calculate retry attempt : " + outBoundFile.getName()), e);
            return true;
        }
    }
 
    /**
     * Get follow up documents
     * 
     * @return list of messages
     * @throws Exception
     */
    public MessageListDTO getFollowUpDocuments() {

        File followUpStore = new File(followUpMsgDir);
        
        List<File> files;
        files = (List<File>) FileUtils.listFiles(followUpStore, EXT_ARR, true);
        MessageListDTO messageListDTO = new MessageListDTO();
        List<MessageDTO> messages = new ArrayList<MessageDTO>(files.size());

        for (File file : files) {

            FileInputStream fileInputStream = null;
            try {

                fileInputStream = new FileInputStream(file.getAbsolutePath());
                Path path = Paths.get(file.getPath());
                MessageDTO messageDTO = new MessageDTO();
                messageDTO.setFileName(file.getName());
                messageDTO.setSenderId(path.getName(path.getNameCount() - 3).toString());
                messageDTO.setReceiverId(path.getName(path.getNameCount() - 2).toString());
                messageDTO.setFileData(IOUtils.toByteArray(fileInputStream));
                messages.add(messageDTO);
                messageListDTO.setMessages(messages);
            } catch (Exception ex) {
                LOGGER.error("Unable to get the followup message", ex);
            } finally {
                IOUtils.closeQuietly(fileInputStream);
            }
        }
        return messageListDTO;
    }

    /**
     * send follow up documents
     * 
     * @param messageDTO
     *            the message dto
     * @return true, if successful 
     * @throws Exception
     */
    public boolean sendFollowUpDocuments(MessageDTO message) {

        String senderId = null;
        String receiverId = null;
        String fileName = null;
        File file = null;
        InputStream is = null;

        try {
            senderId = message.getSenderId();
            receiverId = message.getReceiverId();
            fileName = followUpMsgDir + File.separatorChar + senderId.replace(":", "_") + File.separatorChar
                    + receiverId.replace(":", "_") + File.separatorChar
                    + message.getFileName();
            file = new File(fileName);
            
            is = new FileInputStream(file);

            DocumentDTO documentDTO = new DocumentDTO();
            documentDTO.setFileData(IOUtils.toByteArray(is));
            documentDTO.setLicenseId("PREPAID");
            sendDocument(documentDTO, DS_NAME, true);
        } catch (Exception ex) {

            LOGGER.error("Unable to send the followup message", ex);
            return false;
        } finally {
            IOUtils.closeQuietly(is);
            if (file != null)
            file.delete();
        }
        return true;
    }

    /**
     * Creates the user.
     * 
     * @param userDTO
     *            the user dto
     * @return true, if successful
     */
    public boolean createUser(UserDTO userDTO) {

        if (userDTO.getUserName() == null || userDTO.getUserName().isEmpty()) {
            throw new ServerException(OutboundConstants.INVALID_DATA);
        }

        if (!UserRole.isValidRole(userDTO.getRoleName())) {
            throw new ServerException(OutboundConstants.INVALID_ROLE);
        }

        OutboundBO outboundBO = null;
        try {

            outboundBO = new OutboundBO(DS_NAME);
            outboundBO.createUser(userDTO);

        } catch (Throwable e) {
            LOGGER.error("Unable to create new user", e);
            throw new ServerException(e);
        }
        return true;
    }

    /**
     * Configure participant email ids in the data store.
     * 
     * @param emailDTOs
     *            the email dtos
     * @param userId
     *            the user id
     * @return true, if successful
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws ClassNotFoundException
     *             the class not found exception
     * @throws Excepion 
     *             the exception
     */
    public boolean configureParticipantEmail(List<EmailDTO> emailDTOs)
            throws IOException, ClassNotFoundException, Exception {

        if (emailDTOs == null || emailDTOs.isEmpty()) {
            throw new ServerException(OutboundConstants.INVALID_DATA);
        }

        OutboundBO outboundBO = null;

        try {

            outboundBO = new OutboundBO(DS_NAME);
            outboundBO.updateParticipantEmail(emailDTOs);

        } catch (Throwable e) {
            LOGGER.error("Unable to update participant email", e);
            throw new ServerException(e);
        }

        return true;
    }

    /**
     * Get participant email ids in the data store.
     * 
     * @param participantId
     *            the participant Id
     * @return array of emaildIds
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws ClassNotFoundException
     *             the class not found exception
     * @throws Excepion 
     *             the exception
     */
    public String[] getParticipantEmail(String participantId) throws IOException,
            ClassNotFoundException, Exception {

        if (participantId == null || participantId.isEmpty()) {
            throw new ServerException(OutboundConstants.INVALID_DATA);
        }

        OutboundBO outboundBO = null;
        String[] emailIds = null;
        try {

            outboundBO = new OutboundBO(DS_NAME);
            emailIds = outboundBO.getParticipantEmail(participantId);

        } catch (Throwable e) {
            LOGGER.error("Unable to update participant email", e);
            throw new ServerException(e);
        }

        return emailIds;
    }

    /**
     * Download documents.
     *
     * @param fileName 
     *          the file name
     * @param contextPath 
     *          the context path
     * @return the file
     * @throws Excepion 
     *             the exception
     */
    public File downloadDocuments(String fileName, String contextPath) throws Exception {

        try {

            if (fileName != null) {

                OutboundBO outboundBO = new OutboundBO(DS_NAME);
                /*AuditLog auditLog = outboundBO.getInvoiceFailureMessage(FilenameUtils.removeExtension(fileName).replace("_", ":"));
                File invoiceFile = new File(followUpMsgDir + File.separatorChar + auditLog.getSenderId().replace(":", "_") + File.separatorChar
                        + auditLog.getReceiverId().replace(":", "_") + File.separatorChar + fileName);*/

                //return invoiceFile;
                return null;
            }
        } catch (Exception e) {
            LOGGER.error("Unable to download the follow up file: " + fileName, e);
        }
        return null;
    }

    /**
     * Delete messages of the participant from the server.
     * 
     * @param msgRemoverDTO
     *            the msg remover dto
     * @param userId
     *            the user id
     * @return true, if successful
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws ClassNotFoundException
     *             the class not found exception
     * @throws Excepion 
     *             the exception
     */
    public boolean deleteMessages(MessageRemoverDTO msgRemoverDTO, String userId)
            throws IOException, ClassNotFoundException, Exception {

        if (msgRemoverDTO == null || msgRemoverDTO.getParticipantId() == null
                || msgRemoverDTO.getMessageIds() == null
                || msgRemoverDTO.getMessageIds().isEmpty()) {
            throw new ServerException(OutboundConstants.INVALID_DATA);
        }

        OutboundBO outboundBO = new OutboundBO(DS_NAME);
        Boolean result = outboundBO.deleteMessages(msgRemoverDTO.getMessageIds());
        return result;
    }

    /**
     * Checks if the paricipant is valid against the customizationID
     * 
     * @param participantId
     *            the participant id
     * @param userId
     *            the user id
     * @return true, if is valid participant
     */
    public boolean isValidparticipantAgainstCustomizationID(String participantId, String userId) {

        OxalisOutboundComponent oxalisOutboundComponent = new OxalisOutboundComponent();
        LookupService lookupService = oxalisOutboundComponent.getLookupService();
        Endpoint endpoint = null;
        String[] values = participantId.split("\\^");

        try {
            Header header = Header.newInstance()
                    .receiver(ParticipantIdentifier.of(values[0]))
                    .documentType(DocumentTypeIdentifier.of(values[1]))
                    .process(ProcessIdentifier.of(EHFConstants.INVOICE_TWO_DOT_ONE_PROFILE_ID.getValue()));
            endpoint = lookupService.lookup(header);

        } catch (Exception e) {
            LOGGER.error("Unable to validate participant :" + participantId, e);
            return false;
        }
        return (endpoint.getAddress() != null);
    }

    private AuditLog prepareAuditInfo(String transmissionIdentifier, DocumentDTO documentDTO, String userId, boolean isResendInvoice, String exceptionMsg) {

        AuditLog audit = new AuditLog();

        if (transmissionIdentifier != null) {
            audit.setDescription(transmissionIdentifier);
            audit.setStatus(1);
        } else {
            audit.setStatus(0);
        }
        audit.setComments(exceptionMsg);
        audit.setSenderId(documentDTO.getSenderId());
        audit.setReceiverId(documentDTO.getReceiverId());
        if (isResendInvoice) {
            audit.setEventType(AuditEvent.RESEND_INVOICE.toString());
        } else {
            audit.setEventType(AuditEvent.SEND_INVOICE.toString());
        }
        audit.setEventDate(new Date());
        audit.setLicenseId(documentDTO.getLicenseId());
        audit.setUserId(userId);
        audit.setEventId(EHFConstants.EHF_THREE_DOT_ZERO_CUSTOMIZATION_ID.getValue());
        return audit;
    }
}
