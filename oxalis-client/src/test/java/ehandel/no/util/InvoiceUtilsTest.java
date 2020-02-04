/*
 * @(#)InvoiceUtilsTest.java
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
 * The Class InvoiceUtilsTest.
 *
 * @author amuthar
 * @since ehf; Feb 15, 2012
 */
public class InvoiceUtilsTest {

    public InvoiceUtilsTest() {
    }

    /**
     * Test get invoice xml file.
     */
    @Test
    public void getXML() {

        try {

            File file = new File(TestData.INVOICE_FILE_NAME);
            if (file.exists()) {
                file.delete();
            }

            InvoiceDTO invoiceDTO = TestData.getEHFInvoiceInfo(TestData.INVOICE);

            boolean result = InvoiceUtils.isValidInvoiceAmount(invoiceDTO);

            Assert.assertTrue(result);

            InvoiceUtils.generateEHFInvoiceXML(invoiceDTO, TestData.INVOICE_FILE_NAME);

            Assert.assertTrue(file.exists());

            Assert.assertTrue(new SchemaValidator().validateInvoice(file.getAbsolutePath()));

            byte[] xmlByteArray = InvoiceUtils.generateEHFInvoiceXML(invoiceDTO);
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
            InputStream is = getClass().getResourceAsStream("invoice-ehf.xml");

            InvoiceDTO invoiceDTO = InvoiceUtils.getEHFInvoice(is);

            File newFile = new File("invoice-out.xml");
            if (newFile.exists()) {
                newFile.delete();
            }

            InvoiceUtils.generateEHFInvoiceXML(invoiceDTO, newFile.getAbsolutePath());

            Assert.assertNotNull(invoiceDTO);

            Assert.assertTrue(new SchemaValidator().validateInvoice(newFile.getAbsolutePath()));

        } catch (Throwable ex) {
            Assert.fail(ex.getMessage());
        }
    }

    /**
     * Test get EHF V2 invoice xml file.
     */
    @Test
    public void getEHFV2XML() {

        try {

            File file = new File(TestData.INVOICE_EHF_V2_FILE_NAME);
            if (file.exists()) {
                file.delete();
            }

            InvoiceDTO invoiceDTO = TestData.getEHFInvoiceInfo(TestData.INVOICE);

            boolean result = InvoiceUtils.isValidInvoiceAmount(invoiceDTO);

            Assert.assertTrue(result);

            InvoiceUtils.generateEHFV2InvoiceXML(invoiceDTO, TestData.INVOICE_EHF_V2_FILE_NAME);

            Assert.assertTrue(file.exists());

            Assert.assertTrue(new SchemaValidator().validateEHFV2Invoice(file.getAbsolutePath()));

            byte[] xmlByteArray = InvoiceUtils.generateEHFV2InvoiceXML(invoiceDTO);
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
            InputStream is = getClass().getResourceAsStream("invoice-ehf-v2.xml");

            InvoiceDTO invoiceDTO = InvoiceUtils.getEHFV2Invoice(is);

            File newFile = new File("invoice-ehf-out.xml");
            if (newFile.exists()) {
                newFile.delete();
            }

            InvoiceUtils.generateEHFV2InvoiceXML(invoiceDTO, newFile.getAbsolutePath());

            Assert.assertNotNull(invoiceDTO);

            Assert.assertTrue(new SchemaValidator().validateEHFV2Invoice(newFile.getAbsolutePath()));

            File newFile1 = new File("invoice-ehf-out1.xml");
            if (newFile1.exists()) {
                newFile1.delete();
            }
            invoiceDTO.setIsCreditNoteAgaintInvoice(Boolean.TRUE);
            InvoiceUtils.generateEHFV2InvoiceXML(invoiceDTO, newFile1.getAbsolutePath());
            Assert.assertTrue(new SchemaValidator().validateEHFV2Invoice(newFile.getAbsolutePath()));

        } catch (Throwable ex) {
            Assert.fail(ex.getMessage());
        }
    }
}
