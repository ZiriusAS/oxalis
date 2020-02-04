/*
 * @(#)PrintServiceClientTest.java
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

import itella.api.FormCode;
import itella.api.PostType;
import itella.api.PrintInvoiceDTO;
import itella.api.SmsDTO;
import itella.service.client.PrintServiceClient;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 *
 * @author vasanthis
 */
public class PrintServiceClientTest {

    /**
     * Test send invoice.
     */
    @Test
    public void testSendInvoice() {

        try {

            PrintInvoiceDTO printDTO = new PrintInvoiceDTO();
            printDTO.setUserId("OFS Test");
            printDTO.setLicenseId("GnJWSwptwj");
            printDTO.setPostType(PostType.A_POST);
            printDTO.setFormCode(FormCode.GIRO_TEMPLATE);
            InputStream is = PrintServiceClientTest.class.getResourceAsStream("/validInvoice.xml");
            printDTO.setFileData(IOUtils.toByteArray(is));

            PrintServiceClient.getInstance().initialize("http://localhost:9090/itella/sendToPrintServer");
            String result = PrintServiceClient.getInstance().sendToPrintService(printDTO, "en", false, "superadmin@zirius.no", "6c817128e56418ff38249812af803c3b");

            System.out.println("RESULT IS : " + result);

        } catch (Exception ex) {
            System.out.println("Exception Is : " + ex.getMessage());
        } 
    }

    /**
     * Test send sms.
     */
    @Test
    public void testSendSms() {

        try {

            SmsDTO smsDTO = new SmsDTO();
            smsDTO.setCellular("00919841722440");
            smsDTO.setMsg("Test Msg From Zerp");

            String status = PrintServiceClient.getInstance().sendSms(smsDTO, "superadmin@zirius.no", "6c817128e56418ff38249812af803c3b");
            System.out.println("Status IS : " + status);

        } catch (Exception ex) {
            System.out.println("Exception Is : " + ex.getMessage());
        }
    }
}