/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.difi.oxalis.service.util;

import com.zirius.zerp.rc.C2ZiriusHandlingExceptionType;
import com.zirius.zerp.rc.C2ZiriusReceptionAcknowledgementType;
import com.zirius.zerp.rc.ExceptionType;
import com.zirius.zerp.rc.StandardBusinessDocument;
import com.zirius.zerp.rc.StandardBusinessDocumentHeader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author zerp labs
 */
public class C2ReceiptGenerator {
    
    private static C2ReceiptGenerator singleton = null;
    
    private static String VERSION = "1.0";
    private String ePEPPOLReceiptPath = "";
    
    private Unmarshaller epUnmarshaller = null;
    private Marshaller epMarshaller = null;
    
    private C2ReceiptGenerator() throws JAXBException, IOException {
        
            ePEPPOLReceiptPath = PropertyUtil.getProperty(Property.EPEPPOL_RECEIPT_PATH);
        
            JAXBContext jaxbContext = JAXBContext.newInstance(StandardBusinessDocument.class,
                                                              C2ZiriusReceptionAcknowledgementType.class,
                                                              C2ZiriusHandlingExceptionType.class);
            epUnmarshaller = jaxbContext.createUnmarshaller();
            epMarshaller = jaxbContext.createMarshaller();
        
    }
    
    public static C2ReceiptGenerator getInstance() throws JAXBException, IOException {
        
        if(singleton == null) {
            singleton = new C2ReceiptGenerator();
        }
        
        return singleton;
    }
    
    
    /**
     * Generates acknowledgement receipt (C2) from standard business document. 
     * 
     * @param sdb
     * @throws JAXBException 
     */
    public void  generateAcknowledgementFromSDB(byte[] sdbByte) throws JAXBException, IOException {
        
        StandardBusinessDocumentHeader sdbh = extractSDBH(sdbByte);
        
        C2ZiriusReceptionAcknowledgementType ack = generateAcknowledgement(sdbh);
        
        QName name = new QName("urn:fdc:difi.no:2017:payment:extras-1","C2ZiriusReceptionAcknowledgement");
            
        JAXBElement<C2ZiriusReceptionAcknowledgementType> element 
                = new JAXBElement<C2ZiriusReceptionAcknowledgementType>(name,C2ZiriusReceptionAcknowledgementType.class, ack);
        
        generateReceiptXML(element, getInstanceIdentifier(sdbh), "acknowledgement");

    }
    
    /**
     * Generates exception receipt (C2) from standard business document. 
     * 
     * @param sdb
     * @throws JAXBException 
     */
    public void  generateExceptionFromSDB(byte[] sdbByte, Exception ex) throws JAXBException, IOException {
        
        StandardBusinessDocumentHeader sdbh = extractSDBH(sdbByte);
        
        C2ZiriusHandlingExceptionType exception = generateExceptionAcknowledgement(sdbh, 
                ex.getClass().toString(), ex.getMessage());
        
        QName name = new QName("urn:fdc:difi.no:2017:payment:extras-1", "C2ZiriusHandlingException");
            
        JAXBElement<C2ZiriusHandlingExceptionType> element 
                = new JAXBElement<C2ZiriusHandlingExceptionType>(name,C2ZiriusHandlingExceptionType.class, exception);
        
        generateReceiptXML(element, getInstanceIdentifier(sdbh), "exception");

    }
    
    /**
     *  parse the byte array into standard business document and extract the header element instance. 
     * 
     * @param sdb
     * @return
     * @throws JAXBException 
     */
    private StandardBusinessDocumentHeader extractSDBH(byte[] sdb) throws JAXBException {
        
        Source source = new StreamSource(new ByteArrayInputStream(sdb));

        JAXBElement<StandardBusinessDocument> element = epUnmarshaller.unmarshal(source,StandardBusinessDocument.class);
        StandardBusinessDocument standardBusinessDocument = element.getValue();
        
        return standardBusinessDocument.getStandardBusinessDocumentHeader();
    }
    
    /**
     * get the instance identifier
     */
    private String getInstanceIdentifier(StandardBusinessDocumentHeader sdbh) {
        
        return sdbh.getDocumentIdentification().getInstanceIdentifier();
    }
    
    /**
     * Converts the object into XML content. Store the XML content as a document in the epeppol location. 
     * 
     * @param object 
     */
    private void generateReceiptXML(Object object, String filename, String docType) throws IOException, JAXBException {
        
        File receipt = File.createTempFile(filename, "."+docType+".xml");
        
        
        epMarshaller.marshal(object, receipt);
        
        // move to epeppol receipt location
        receipt.renameTo(new File(ePEPPOLReceiptPath + File.separator + receipt.getName()));
    }
    
    public static C2ZiriusReceptionAcknowledgementType generateAcknowledgement(StandardBusinessDocumentHeader sbdh) {
        
        C2ZiriusReceptionAcknowledgementType ack = new C2ZiriusReceptionAcknowledgementType();
        ack.setStandardBusinessDocumentHeader(sbdh);
        ack.setVersion(VERSION);
        
        return ack;
    }
    
    public static C2ZiriusHandlingExceptionType generateExceptionAcknowledgement(StandardBusinessDocumentHeader sbdh,
            String errorCode, String errorDesc) {
        
        C2ZiriusHandlingExceptionType exception = new C2ZiriusHandlingExceptionType();
        exception.setStandardBusinessDocumentHeader(sbdh);
        
        ExceptionType exceptionType = new ExceptionType();
        
        exceptionType.setCode(errorCode);
        exceptionType.setDescription(errorDesc);
        
        exception.setException(exceptionType);
        exception.setVersion(VERSION);
        
        return exception;
    }
    
}
