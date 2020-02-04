/*
 * @(#)AccessPointClientTest.java
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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import ehandel.no.EHFCustomizationID;
import eu.peppol.outbound.api.DocumentDTO;
import eu.peppol.outbound.api.EmailDTO;
import eu.peppol.outbound.api.MessageDTO;
import eu.peppol.outbound.api.MessageListDTO;
import eu.peppol.outbound.api.MessageRemoverDTO;
import eu.peppol.outbound.api.UserDTO;
import ehandel.no.dto.AccesspointDetails;

/**
 * The Class AccessPointClientTest.
 * 
 * @author senthilkumarn
 */
public class AccessPointClientTest {

    /**
     * Test send, receive & remove messages.
     * 
     * @throws Exception
     */
    @Test
    public void testSendReceiveRemove() throws Exception {

        try {
            InputStream is =
                    ClassLoader.getSystemClassLoader().getClass().getResourceAsStream(
                            "/no/difi/oxalis/invoice-ehf.xml");
            DocumentDTO documentDTO = new DocumentDTO();
            documentDTO.setFileData(IOUtils.toByteArray(is));
            documentDTO.setSenderId("9908:992183888");
            documentDTO.setReceiverId("9908:986559469");

            String messageId = AccessPointClient.sendInvoice(documentDTO);
            Assert.assertNotNull(messageId);

            MessageListDTO messageListDTO = AccessPointClient.receiveInvoice("9908:986559469");
            Assert.assertNotNull(messageListDTO);

            List<String> messageIds = new ArrayList<String>(1);
            messageIds.add(messageId);
            boolean result = AccessPointClient.markAsRead(messageIds);
            Assert.assertTrue(result);

            messageListDTO = AccessPointClient.receiveInvoice(new Date());
            Assert.assertNotNull(messageListDTO.getSynchronizedDate());

            MessageDTO messageDTO = messageListDTO.getMessages().get(0);

            MessageRemoverDTO msgRemoverDTO = new MessageRemoverDTO();
            msgRemoverDTO.setParticipantId(messageDTO.getReceiverId());
            msgRemoverDTO.setMessageIds(messageIds);

            result = AccessPointClient.removeMessage(msgRemoverDTO);
            Assert.assertTrue(result);

            List<EmailDTO> emailDTOList = new ArrayList<EmailDTO>(1);
            EmailDTO emailDTO = new EmailDTO();
            emailDTO.setParticipantId("9908:992183888");
            emailDTO.setEmailId("zirius@ofs.com");
            emailDTOList.add(emailDTO);

            result = AccessPointClient.configureParticipantEmail(emailDTOList);
            Assert.assertTrue(result);

        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
    
    /**
     * Testget participant email.
     *
     * @throws Exception the exception
     */
    @Test
    public void testgetParticipantEmail() throws Exception {
        
        String[] emailIds = AccessPointClient.getParticipantEmail("9908:992183888");
        Assert.assertNotNull(emailIds);
    }

    /**
     * Test create user.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void testCreateUser() throws Exception {
        
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("admin1@zirius.no");
        userDTO.setPassword("admin1@zirius.no");
        userDTO.setRoleName("AccessPointAdmint");
        boolean result = AccessPointClient.createUser(userDTO);
        Assert.assertTrue(result);
    }

    /**
     * Test send order.
     *
     * @throws Exception the exception
     */
    @Test
    public void testSendOrder() throws Exception {

        InputStream is =
                    ClassLoader.getSystemClassLoader().getClass().getResourceAsStream(
                            "/ehandel/no/util/order-ehf.xml");
            DocumentDTO documentDTO = new DocumentDTO();
            documentDTO.setFileData(IOUtils.toByteArray(is));
            documentDTO.setSenderId("9908:986920021");
            documentDTO.setReceiverId("9908:986920021");

            String messageId = AccessPointClient.sendOrder(documentDTO);
            Assert.assertNotNull(messageId);
    }

    @Test
    public void isValidParticipant() throws Exception {


        try {
             boolean messageId = AccessPointClient.isValidParticipant("9908:986920080", "superadmin@zirius.no", "6c817128e56418ff38249812af803c3b", EHFCustomizationID.EHF_INVOICE_TWO_DOT_ZERO);
            Assert.assertTrue(messageId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void isValidParticipantWithoutUser() throws Exception {


        try {
             boolean messageId = AccessPointClient.isValidParticipant("9908:986920080", EHFCustomizationID.EHF_INVOICE_TWO_DOT_ZERO);
            Assert.assertTrue(messageId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void isGetAccessPoint() throws Exception {

        try {

            AccesspointDetails endpointData = AccessPointClient.getAccesspointDetails("9908:986920080");
            Assert.assertNotNull(endpointData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
