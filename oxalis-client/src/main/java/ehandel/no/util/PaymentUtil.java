/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ehandel.no.util;

import ehandel.no.EHFConstants;
import ehandel.no.dto.ExceptionDTO;
import ehandel.no.dto.HandlingExceptionDTO;
import ehandel.no.dto.MetadataDTO;
import ehandel.no.dto.RequestAcknowledgementDTO;
import ehandel.no.paymentextras.ExceptionType;
import ehandel.no.paymentextras.HandlingExceptionType;
import ehandel.no.paymentextras.MetadataType;
import ehandel.no.paymentextras.ReceptionAcknowledgementType;
import ehandel.no.paymentextras.StandardBusinessDocumentHeader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 *
 * @author zerp labs
 */
public class PaymentUtil {
    
    private static JAXBContext context;
    
    private static JAXBContext getMetadataJAXBContext() throws Exception {
        if (context == null) {
            context = JAXBContext.newInstance(MetadataType.class);
        }
        return context;
    }
    
    private static JAXBContext getRC4JAXBContext() throws Exception {
        if (context == null) {
            context = JAXBContext.newInstance(ReceptionAcknowledgementType.class);
        }
        return context;
    }
    
    private static JAXBContext getRC4bJAXBContext() throws Exception {
        if (context == null) {
            context = JAXBContext.newInstance(HandlingExceptionType.class);
        }
        return context;
    }
    
    private static JAXBContext getSBDHJAXBContext() throws Exception {
        if (context == null) {
            context = JAXBContext.newInstance(StandardBusinessDocumentHeader.class);
        }
        return context;
    }
    
    public PaymentUtil() {}
    
    private static Marshaller getRC4Marshaller() throws JAXBException {

        final JAXBContext context = JAXBContext.newInstance(ReceptionAcknowledgementType.class);

        final Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, EHFConstants.CHAR_ENCODING.getValue());
        return marshaller;
    }
    
    private static Marshaller getMetadataMarshaller() throws JAXBException {

        final JAXBContext context = JAXBContext.newInstance(MetadataType.class);

        final Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, EHFConstants.CHAR_ENCODING.getValue());

        return marshaller;
    }
    
    private static Marshaller getRC4bMarshaller() throws JAXBException {

        final JAXBContext context = JAXBContext.newInstance(HandlingExceptionType.class);

        final Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, EHFConstants.CHAR_ENCODING.getValue());

        return marshaller;
    }  
    
    private static Marshaller getSBDHMarshaller() throws JAXBException {

        final JAXBContext context = JAXBContext.newInstance(StandardBusinessDocumentHeader.class);

        final Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, EHFConstants.CHAR_ENCODING.getValue());

        return marshaller;
    }
    
    public static byte[] generateMetadataXML(MetadataDTO metadataDTO) throws JAXBException {
        
        MetadataType metadata = convert(metadataDTO);
        
        Marshaller marshaller = getMetadataMarshaller();
        
        JAXBElement<MetadataType> element = 
                new JAXBElement<>(new QName("urn:fdc:difi.no:2017:payment:extras-1", "Metadata"),
                MetadataType.class,
                metadata);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        marshaller.marshal(element, out);

        return out.toByteArray();
    }
    
    private static MetadataType convert(MetadataDTO dto) {
        
        MetadataType metadata = new MetadataType();
        
        metadata.setVersion(dto.getVersion());
        metadata.setCustomerIdentifier(dto.getCustomerIdentifier());
        metadata.setDivisionIdentifier(dto.getDivisionIdentifier());
        metadata.setUserIdentifier(dto.getUserIdentifier());
        
        return metadata;
    }
    
    public static MetadataDTO getMetadata(InputStream in) throws Exception {
        
        final Unmarshaller unmarshaller = getMetadataJAXBContext().createUnmarshaller();
        MetadataType metadata = (MetadataType) unmarshaller.unmarshal(in);

        return convert(metadata);
    }
    
    private static MetadataDTO convert(MetadataType metadata) {
        
        MetadataDTO dto = new MetadataDTO();
        
        dto.setVersion(metadata.getVersion());
        dto.setCustomerIdentifier(metadata.getCustomerIdentifier());
        dto.setDivisionIdentifier(metadata.getDivisionIdentifier());
        dto.setUserIdentifier(metadata.getUserIdentifier());
        
        return dto;
    }
    
    public static byte[] generateRequestAcknowledgementXML(RequestAcknowledgementDTO dto) throws Exception {
        
        ReceptionAcknowledgementType receptionAcknowledgement = convert(dto);
        
        Marshaller marshaller = getRC4Marshaller();
        
        JAXBElement<ReceptionAcknowledgementType> element = 
                new JAXBElement<>(new QName("urn:fdc:difi.no:2017:payment:extras-1", "ReceptionAcknowledgement"),
                ReceptionAcknowledgementType.class,
                receptionAcknowledgement);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        marshaller.marshal(element, out);

        return out.toByteArray();
    }
    
    private static ReceptionAcknowledgementType convert(RequestAcknowledgementDTO dto) throws Exception {
        
        ReceptionAcknowledgementType receptionAcknowledgement = new ReceptionAcknowledgementType();
        
        receptionAcknowledgement.setVersion(dto.getVersion());
        receptionAcknowledgement.setStandardBusinessDocumentHeader(convertToSBDH(dto.getSbdh().getBytes()));
        
        return receptionAcknowledgement;
    }    
    
    public static RequestAcknowledgementDTO getRequestAcknowledgement(InputStream in) throws Exception {
        
        final Unmarshaller unmarshaller = getRC4JAXBContext().createUnmarshaller();
        ReceptionAcknowledgementType receptionAcknowledgementType = 
                (ReceptionAcknowledgementType) unmarshaller.unmarshal(in);

        return convert(receptionAcknowledgementType);
    }
    
    private static RequestAcknowledgementDTO convert(ReceptionAcknowledgementType receptionAcknowledgement) throws JAXBException {
        
        RequestAcknowledgementDTO dto = new RequestAcknowledgementDTO();
        
        dto.setVersion(receptionAcknowledgement.getVersion());
        dto.setSbdh(String.valueOf(convert(receptionAcknowledgement.getStandardBusinessDocumentHeader())));
        
        return dto;
    }
    
    public static byte[] generateHandlingExceptionXML(HandlingExceptionDTO dto) throws Exception {
        
        HandlingExceptionType handlingException = convert(dto);
        
        Marshaller marshaller = getRC4bMarshaller();
        
        JAXBElement<HandlingExceptionType> element = 
                new JAXBElement<>(new QName("urn:fdc:difi.no:2017:payment:extras-1", "HandlingException"),
                HandlingExceptionType.class,
                handlingException);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        marshaller.marshal(element, out);

        return out.toByteArray();
    }
    
    public static HandlingExceptionType convert(HandlingExceptionDTO dto) throws Exception {
        
        HandlingExceptionType handlingException = new HandlingExceptionType();
        
        handlingException.setVersion(dto.getVersion());
        
        ExceptionType exception = new ExceptionType();
        exception.setCode(dto.getException().getCode());
        exception.setDescription(dto.getException().getDescription());
        handlingException.setException(exception);
        
        handlingException.setStandardBusinessDocumentHeader(convertToSBDH(dto.getSbdh().getBytes()));
        
        return handlingException;
    }
    
    public static HandlingExceptionDTO getHandlingException(InputStream in) throws Exception {
        
        final Unmarshaller unmarshaller = getRC4bJAXBContext().createUnmarshaller();
        HandlingExceptionType handlingException = 
                (HandlingExceptionType) unmarshaller.unmarshal(in);

        return convert(handlingException);
    }
    
    private static HandlingExceptionDTO convert(HandlingExceptionType handlingException) throws JAXBException {
        
        HandlingExceptionDTO dto = new HandlingExceptionDTO();
        
        dto.setVersion(handlingException.getVersion());
        
        ExceptionDTO exceptionDto = new ExceptionDTO();
        exceptionDto.setCode(handlingException.getException().getCode());
        exceptionDto.setDescription(handlingException.getException().getDescription());
        dto.setException(exceptionDto);
        
        dto.setSbdh(String.valueOf(convert(handlingException.getStandardBusinessDocumentHeader())));
        
        return dto;
    }
    
    public static StandardBusinessDocumentHeader convertToSBDH(byte[] sbdh) throws Exception {
        
        final Unmarshaller unmarshaller = getSBDHJAXBContext().createUnmarshaller();
        
        JAXBElement<StandardBusinessDocumentHeader> element = 
                (JAXBElement<StandardBusinessDocumentHeader>) unmarshaller.unmarshal(new ByteArrayInputStream(sbdh));
        
        return element.getValue();
    }
    
    public static byte[] convert(StandardBusinessDocumentHeader sbdh) throws JAXBException {
        
        Marshaller marshaller = getSBDHMarshaller();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        marshaller.marshal(sbdh, out);

        return out.toByteArray();
    }
}
