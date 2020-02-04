/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.difi.oxalis.service.transmission;

import eu.peppol.outbound.api.DocumentDTO;
import eu.peppol.outbound.api.EmailDTO;
import eu.peppol.outbound.api.MessageDTO;
import eu.peppol.outbound.api.MessageRemoverDTO;
import eu.peppol.outbound.api.UserDTO;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author aktharhussainis
 */
public class OutboundServiceTest extends BaseServiceTest {
    
    private static final String userId = "superadmin@zirius.no";
    private final OutboundService outboundService = OutboundService.getInstance();
    
    @BeforeClass
    public static void setUp() throws Exception {
        init();
    }
    
    @Test
    public void sendDocumentTest() throws Throwable {
        
        System.out.println("Inside Test 1");
        
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("invoice-ehf.xml");

        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setSenderId("9908:992183888");
        documentDTO.setReceiverId("9908:986920080");
        documentDTO.setFileData(IOUtils.toByteArray(is));
        documentDTO.setLicenseId("PREPAID");

        String messageId = outboundService.sendDocument(documentDTO, userId, false);
        Assert.assertNotNull(messageId);  
    }

    @Test
    public void testGetMessageInfo() throws Throwable {

        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("invoice-ehf.xml");

        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setSenderId("9908:992183888");
        documentDTO.setReceiverId("9908:986920080");
        documentDTO.setFileData(IOUtils.toByteArray(is));
        documentDTO.setLicenseId("PREPAID");

        String messageId = outboundService.sendDocument(documentDTO, userId, false);
        Assert.assertNotNull(outboundService.getMessageInfo(messageId, userId));
    }

    @Test
    public void testGetFileContentByFileName() throws Throwable {

        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("invoice-ehf.xml");

        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setSenderId("9908:992183888");
        documentDTO.setReceiverId("9908:986920080");
        documentDTO.setFileData(IOUtils.toByteArray(is));
        documentDTO.setLicenseId("PREPAID");

        String messageId = outboundService.sendDocument(documentDTO, userId, false);
        Assert.assertNotNull(outboundService.getFileContentByFileName(messageId));
    }

    @Test
    public void testResend() {
        outboundService.resend();
    }

    @Test
    public void testGetFollowUpDocuments() {
        outboundService.getFollowUpDocuments();
    }

    @Test
    public void testSendFollowUpDocuments() {

        MessageDTO message = new MessageDTO();
        message.setSenderId("9908_986920080");
        message.setReceiverId("9908_986559469");
        message.setFileName("23272608.2.1518082022534.JavaMail.vinothinir@PC1465.doc.xml");
        outboundService.sendFollowUpDocuments(message);
    }

    /**
     * Test create user
     */
    @Test
    public void createUser() throws Throwable {

        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("Zerpuser" + System.currentTimeMillis());
        userDTO.setPassword("Zerpuser");
        userDTO.setRoleName("AccessPointAdmin");

        outboundService.createUser(userDTO);
    }

    /**
     * Test configure participant email
     */
    @Test
    public void configureParticipantEmail() throws Throwable {

        List<EmailDTO> emailDTOs = new ArrayList<EmailDTO>(1);
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setEmailId("vinothini.rajamanickam@object-frontier.com");
        emailDTO.setParticipantId("9908:986559480");
        emailDTOs.add(emailDTO);

        boolean result = outboundService.configureParticipantEmail(emailDTOs);
        Assert.assertTrue(result);
    }

    /**
     * Test get participant email
     */
    @Test
    public void getParticipantEmail() throws Throwable {

        String ParticipantId = "9908:992183888";

        outboundService.getParticipantEmail(ParticipantId);
        Assert.assertTrue(true);
    }

    /**
     * Test download documents
     */
    @Test
    public void testDownloadDocuments() throws Throwable {

        String fileName = "583229940.6.1525703534976.JavaMail.vinothinir@PC1395.xml";

        outboundService.downloadDocuments(fileName, null);
        Assert.assertNotNull(true);
    }

    /**
     * Test delete messages
     */
    @Test
    public void testDeleteMessages() throws Throwable {

        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("invoice-ehf.xml");

        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setSenderId("9908:992183888");
        documentDTO.setReceiverId("9908:986920080");
        documentDTO.setFileData(IOUtils.toByteArray(is));
        documentDTO.setLicenseId("PREPAID");

        String messageId = outboundService.sendDocument(documentDTO, userId, false);
        Assert.assertNotNull(messageId);

        List<String> messageIds = new ArrayList<String>(1);
        messageIds.add(messageId);

        MessageRemoverDTO messageRemoverDTO = new MessageRemoverDTO();
        messageRemoverDTO.setParticipantId(documentDTO.getReceiverId());
        messageRemoverDTO.setMessageIds(messageIds);

        Assert.assertTrue(outboundService.deleteMessages(messageRemoverDTO, userId));
    }

    /**
     * Test validate participant against customization Id
     */
    @Test
    public void isValidparticipantAgainstCustomizationID() throws Throwable {

        boolean result = outboundService.isValidparticipantAgainstCustomizationID("9908:986920080^" +
                "urn:www.cenbii.eu:transaction:biitrns010:ver2.0:extended:urn:www.peppol.eu:bis:peppol4a:ver2.0:extended:urn:www.difi.no:ehf:faktura:ver2.0",
                userId);
        Assert.assertTrue(result);
    }

/*    @Test
    public void testGetAllMessages() {
        
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date now = formatter.parse("2015-11-07");      
            MessageListDTO messageListDTO =  outboundService.getAllMessages(now, userId);
            
            Assert.assertTrue(messageListDTO.getMessages().size() > 0);
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
    @Test
    public void getAccessPointLastSyncDateTimeTest() throws Throwable {

        outboundService.getAccessPointLastSyncDateTime();
    }*/
}
