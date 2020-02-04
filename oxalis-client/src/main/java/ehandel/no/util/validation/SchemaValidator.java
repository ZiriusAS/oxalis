/*
 * @(#)SchemaValidator.java
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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.apache.xerces.jaxp.validation.XMLSchemaFactory;
import org.xml.sax.SAXException;

/**
 * Utility to validate xml documents against invoice and creditnote schemas.
 *
 * @author senthilkumarn
 * @since ehf; Mar 16, 2012
 */
public class SchemaValidator {

    private static final String INVOICE_SCHEMA_FILE = "/ehandel/Invoice/UBL-Invoice-2.0.xsd";
    private static final String INVOICE_TWO_DOT_ONE_SCHEMA_FILE = "/ehandel/EHFInvoice/UBL-Invoice-2.1.xsd";
    private static final String CREDIT_NOTE_SCHEMA_FILE =
            "/ehandel/CreditNote/UBL-CreditNote-2.0.xsd";
    private static final String CREDIT_NOTE_TWO_DOT_ONE_SCHEMA_FILE =
            "/ehandel/EHFCreditNote/UBL-CreditNote-2.1.xsd";
    private static final String REMINDER_SCHEMA_FILE = "/ehandel/Reminder/UBL-Reminder-2.0.xsd";
    private static final String ORDER_SCHEMA_FILE = "/ehandel/Order/UBL-Order-2.1.xsd";
    private static final String ORDER_RESPONSE_SCHEMA_FILE = "/ehandel/Order/UBL-OrderResponse-2.1.xsd";

    /**
     * Validate xml document against invoice schema.
     */
    public boolean validateInvoice(String xmlDocument) throws FileNotFoundException, IOException,
            SAXException {

        return validate(xmlDocument,
                new StreamSource(this.getClass().getResourceAsStream(INVOICE_SCHEMA_FILE)));
    }

    public boolean validateEHFV2Invoice(String xmlDocument) throws FileNotFoundException, IOException,
           SAXException {

       return validate(xmlDocument,
               new StreamSource(this.getClass().getResourceAsStream(INVOICE_TWO_DOT_ONE_SCHEMA_FILE)));
    }

    /**
     * Validate xml document input stream against invoice schema.
     */
    public boolean validateInvoice(InputStream is) throws FileNotFoundException, IOException,
            SAXException {

        return validate(is,
                new StreamSource(this.getClass().getResourceAsStream(INVOICE_SCHEMA_FILE)));
    }

    public boolean validateEHFV2Invoice(InputStream is) throws FileNotFoundException, IOException,
            SAXException {

        return validate(is,
                new StreamSource(this.getClass().getResourceAsStream(INVOICE_TWO_DOT_ONE_SCHEMA_FILE)));
    }

    /**
     * Validate xml document against credit note schema.
     */
    public boolean validateCreditNote(String xmlDocument) throws FileNotFoundException,
            IOException, SAXException {

        return validate(xmlDocument,
                new StreamSource(this.getClass().getResourceAsStream(CREDIT_NOTE_SCHEMA_FILE)));
    }

    public boolean validateEHFV2CreditNote(String xmlDocument) throws FileNotFoundException,
            IOException, SAXException {

        return validate(xmlDocument,
                new StreamSource(this.getClass().getResourceAsStream(CREDIT_NOTE_TWO_DOT_ONE_SCHEMA_FILE)));
    }

    /**
     * Validate xml document input stream against credit note schema.
     */
    public boolean validateCreditNote(InputStream is) throws FileNotFoundException, IOException,
            SAXException {

        return validate(is,
                new StreamSource(this.getClass().getResourceAsStream(CREDIT_NOTE_SCHEMA_FILE)));
    }

    public boolean validateEHFV2CreditNote(InputStream is) throws FileNotFoundException, IOException,
            SAXException {

        return validate(is,
                new StreamSource(this.getClass().getResourceAsStream(CREDIT_NOTE_TWO_DOT_ONE_SCHEMA_FILE)));
     }

    /**
     * Validate xml document against reminder schema.
     */
    public boolean validateReminder(String xmlDocument) throws FileNotFoundException, IOException,
            SAXException {

        return validate(xmlDocument,
                new StreamSource(this.getClass().getResourceAsStream(REMINDER_SCHEMA_FILE)));
    }

    /**
     * Validate xml document input stream against reminder schema.
     */
    public boolean validateReminder(InputStream is) throws FileNotFoundException, IOException,
            SAXException {

        return validate(is,
                new StreamSource(this.getClass().getResourceAsStream(REMINDER_SCHEMA_FILE)));
    }

    /**
     * Validate xml document input stream against order schema.
     */
    public boolean validateOrder(InputStream is) throws FileNotFoundException, IOException,
            SAXException {

        return validate(is,
                new StreamSource(this.getClass().getResourceAsStream(ORDER_SCHEMA_FILE)));
    }

    /**
     * Validate xml document against order schema.
     */
    public boolean validateOrder(String xmlDocument) throws FileNotFoundException, IOException,
            SAXException {

        return validate(xmlDocument,
                new StreamSource(this.getClass().getResourceAsStream(ORDER_SCHEMA_FILE)));
    }

    /**
     * Validate xml document input stream against order response schema.
     */
    public boolean validateOrderResponse(InputStream is) throws FileNotFoundException, IOException,
            SAXException {

        return validate(is,
                new StreamSource(this.getClass().getResourceAsStream(ORDER_RESPONSE_SCHEMA_FILE)));
    }

    /**
     * Validate xml document against order response schema.
     */
    public boolean validateOrderResponse(String xmlDocument) throws FileNotFoundException, IOException,
            SAXException {

        return validate(xmlDocument,
                new StreamSource(this.getClass().getResourceAsStream(ORDER_RESPONSE_SCHEMA_FILE)));
    }

    /**
     * Validate the schema against xml document.
     */
    private boolean validate(String xmlDocument, Source schemaSource) throws FileNotFoundException,
            IOException, SAXException {

        getValidator(schemaSource).validate(new StreamSource(new FileReader(xmlDocument)));

        return true;
    }

    /**
     * Validate the schema against xml input stream.
     */
    private boolean validate(InputStream is, Source schemaSource) throws FileNotFoundException,
            IOException, SAXException {

        getValidator(schemaSource).validate(new StreamSource(is));

        return true;
    }

    private Validator getValidator(Source schemaSource) throws SAXException {

        XMLSchemaFactory xmlSchemaFactory = new XMLSchemaFactory();
        xmlSchemaFactory.setResourceResolver(new ResourceResolver());

        Schema schema = xmlSchemaFactory.newSchema(schemaSource);
        return schema.newValidator();
    }

    public static void main(String[] args) throws Exception {

        SchemaValidator validator = new SchemaValidator();
        validator.validateInvoice("D:\\dev\\zirius\\oxalis\\oxalis-start-outbound\\src\\test\\resources\\ehandel\\no\\util\\invoice-ehf.xml");
    }
}
