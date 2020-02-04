/*
 * @(#)CreditNoteUtilsTest.java
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

import java.io.File;
import java.io.InputStream;


import ehandel.no.dto.InvoiceDTO;
import ehandel.no.util.validation.SchemaValidator;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * The Class InvoiceConverter.
 *
 * @author amuthar
 * @since ehf; Feb 15, 2012
 */
public class CreditNoteUtilsTest {

    public CreditNoteUtilsTest() {
    }

    /**
     * Test get creditnote xml file.
     */
    @Test
    public void getXML() {

        try {

            File file = new File(TestData.CREDITNOTE_FILE_NAME);
            if (file.exists()) {
                file.delete();
            }

            InvoiceDTO invoiceDTO = TestData.getEHFInvoiceInfo(TestData.CREDITNOTE);

            CreditNoteUtils.generateEHFInvoiceXML(invoiceDTO, TestData.CREDITNOTE_FILE_NAME);

            Assert.assertTrue(file.exists());

            Assert.assertTrue(new SchemaValidator().validateCreditNote(file.getAbsolutePath()));

            byte[] xmlByteArray = CreditNoteUtils.generateEHFInvoiceXML(invoiceDTO);
            Assert.assertNotNull(xmlByteArray);

        } catch (Throwable ex) {
            Assert.fail(ex.getMessage());
        }
    }

    /**
     * Test get EHF invoice info.
     */
    @Test
    public void getEHFInvoice() {

        try {

            File file = new File(TestData.CREDITNOTE_FILE_NAME);
            if (file.exists()) {
                file.delete();
            }

            InvoiceDTO invoiceDTO = TestData.getEHFInvoiceInfo(TestData.CREDITNOTE);

            CreditNoteUtils.generateEHFInvoiceXML(invoiceDTO, TestData.CREDITNOTE_FILE_NAME);

            invoiceDTO = CreditNoteUtils.getEHFInvoice(file);

            Assert.assertNotNull(invoiceDTO);

            Assert.assertTrue(new SchemaValidator().validateCreditNote(file.getAbsolutePath()));

        } catch (Throwable ex) {
            Assert.fail(ex.getMessage());
        }
    }

    /**
     * Test get EHF V2 creditnote xml file.
     */
    @Test
    public void getEHFV2XML() {

        try {

            File file = new File(TestData.CREDITNOTE_EHF_V2_FILE_NAME);
            if (file.exists()) {
                file.delete();
            }

            InvoiceDTO invoiceDTO = TestData.getEHFInvoiceInfo(TestData.CREDITNOTE);

            CreditNoteUtils.generateEHFV2InvoiceXML(invoiceDTO, TestData.CREDITNOTE_EHF_V2_FILE_NAME);

            Assert.assertTrue(file.exists());

            Assert.assertTrue(new SchemaValidator().validateEHFV2CreditNote(file.getAbsolutePath()));

            byte[] xmlByteArray = CreditNoteUtils.generateEHFV2InvoiceXML(invoiceDTO);
            Assert.assertNotNull(xmlByteArray);

        } catch (Throwable ex) {
            Assert.fail(ex.getMessage());
        }
    }

    /**
     * Test get EHF V2 invoice info.
     */
    @Test
    public void getEHFV2Invoice() {

        try {

            InputStream is = getClass().getResourceAsStream("creditnote-ehf-v2.xml");

            InvoiceDTO invoiceDTO = CreditNoteUtils.getEHFV2Invoice(is);

            File newFile = new File("creditnote-ehf-out.xml");
            if (newFile.exists()) {
                newFile.delete();
            }

            CreditNoteUtils.generateEHFV2InvoiceXML(invoiceDTO, newFile.getAbsolutePath());

            Assert.assertNotNull(invoiceDTO);

            Assert.assertTrue(new SchemaValidator().validateEHFV2CreditNote(newFile.getAbsolutePath()));

            invoiceDTO.setIsCreditNoteAgaintInvoice(Boolean.TRUE);
            File newFile1 = new File("creditnote-ehf-out1.xml");
            if (newFile1.exists()) {
                newFile1.delete();
            }

            CreditNoteUtils.generateEHFV2InvoiceXML(invoiceDTO, newFile1.getAbsolutePath());

        } catch (Throwable ex) {
            Assert.fail(ex.getMessage());
        }
    }
}
