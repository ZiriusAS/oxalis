/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.difi.oxalis.service.controller;

import eu.peppol.outbound.api.DocumentDTO;
import eu.peppol.outbound.api.EmailDTO;
import eu.peppol.outbound.api.MessageDTO;
import eu.peppol.outbound.api.MessageRemoverDTO;
import eu.peppol.outbound.api.UserDTO;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import no.difi.oxalis.service.util.TestUtil;
import org.apache.commons.io.IOUtils;
//import static org.easymock.EasyMock.mock;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author aktharhussainis
 */
public class RestControllerTest extends BaseControllerTest {
    
    private final RestController rs = new RestController();
    
    @BeforeClass
    public static void setUp() throws Exception {
        init();
    }
    
    
    /**
     * Test send, receive & remove messages.
     * 
     * @throws Exception
     */
    @Test
    public void testSend() throws Exception {

        try {
            InputStream is =
                    ClassLoader.getSystemClassLoader().getResourceAsStream("invoice-ehf.xml");
            DocumentDTO documentDTO = new DocumentDTO();
            documentDTO.setFileData(IOUtils.toByteArray(is));
            documentDTO.setSenderId("9908:986920080");
            documentDTO.setReceiverId("9908:986559469");
            SecurityContext securityContext;
            securityContext = new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return new Principal() {
                        @Override
                        public String getName() {
                            return "TestUser";
                        }
                    };
                }

                @Override
                public boolean isUserInRole(String string) {
                    return false;
                }

                @Override
                public boolean isSecure() {
                    return true;
                }

                @Override
                public String getAuthenticationScheme() {
                    return "Basic";
                }
            };

            Response response = rs.send(securityContext, TestUtil.streamObject(documentDTO));
            Assert.assertNotNull(response.getEntity());
            
            System.out.println(response);
            
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
    @Test
    public void testGetMessageInfo() {
        
         try {
             String messageId = "1069131391.2.1512557859762.JavaMail.aktharhussainis_pc1550.doc.xml";
             
             Response response = rs.getMessageInfo(messageId);
             Assert.assertNotNull(response.getEntity());
             
             System.out.println(response);
         } catch (Exception e) {
             System.err.println(e);
         }
    }
    
    @Test
    public void testFetchMessageId() {
        
    try {
             String participant = "0088:1234567987654";
             
             Response response = rs.fetchMessageId(participant);
             Assert.assertNotNull(response.getEntity());
             
             System.out.println(response);
         } catch (Exception e) {
             System.err.println(e);
         }
    }
    
    @Test
    public void testReceiveMessageId() {
        
        try {
            Date now = new Date();
            byte[] data = now.toString().getBytes();
            
            Response response = rs.receiveMessageId(data);
            Assert.assertNotNull(response.getEntity());
             
            System.out.println(response);
        } catch(Exception e) {
            System.err.println(e);
        }
    }
    
    @Test
    public void testReceiveUnReadMessageIdForWeb() {
        
        try {           
            Response response = rs.receiveUnReadMessageIdForWeb();
            Assert.assertNotNull(response.getEntity());
             
            System.out.println(response);
        } catch(Exception e) {
            System.err.println(e);
        }   
    }
    
    @Test
    public void testMarkAsRead() {
        
        try {
            byte[] data = null;
            
            Response response = rs.markAsRead(data);
            Assert.assertNotNull(response.getEntity());
             
            System.out.println(response);
        } catch(Exception e) {
            System.err.println(e);
        }
    }
    
    @Test
    public void testMarkAsReadFromWeb() {
        
        try {
            byte[] data = null;
            
            Response response = rs.markAsReadFromWeb(data);
            Assert.assertNotNull(response.getEntity());
             
            System.out.println(response);
        } catch(Exception e) {
            System.err.println(e);
        }
    }
    
    @Test
    public void testGetAccesspointDetails() {
        
        try {
            
            String participantId = "9908:986559469";
            byte[] data = streamObject(participantId);
            
            Response response = rs.getAccesspointDetails(data);
            Assert.assertNotNull(response.getEntity());
             
            System.out.println(response);
        } catch(Exception e) {
            System.err.println(e);
        }
    }

    @Test
    public void testGetAccesspointDetailsByParticipantId() {
        
        try {
            
            String participantId = "9908:925799351";
            
            Response response = rs.getAccesspointDetails(participantId);
            Assert.assertNotNull(response.getEntity());
             
            System.out.println(response);
        } catch(Exception e) {
            System.err.println(e);
        }
    }

    @Test
    public void testGetFileContentByFileName() {

         try {
             String fileName = "564835198.2.1517988893505.JavaMail.aktharhussainis_pc1550.doc.xml";

             Response response = rs.getFileContentByFileName(fileName);
             Assert.assertNotNull(response.getEntity());

             System.out.println(response);
         } catch (Exception e) {
             System.err.println(e);
         }
    }

    @Test
    public void testGetFollowUpDocuments() {

        try {

            Response response = rs.getFollowUpDocuments();
            Assert.assertNotNull(response.getEntity());
             
            System.out.println(response);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    @Test
    public void testSendFollowUpDocuments() {

         try {

            MessageDTO message = new MessageDTO();
            message.setSenderId("9908_986920080");
            message.setReceiverId("9908_986559469");
            message.setFileName("23272608.2.1518082022534.JavaMail.vinothinir@PC1465.doc.xml");
            Response response = rs.sendFollowUpDocuments(message);
            Assert.assertNotNull(response.getEntity());
             
            System.out.println(response);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Test create user
     */
    @Test
    public void testCreateUser() {

         try {

            UserDTO userDTO = new UserDTO();
            userDTO.setUserName("Zerpuser" + System.currentTimeMillis());
            userDTO.setPassword("Zerpuser");
            userDTO.setRoleName("AccessPointAdmin");
            Response response = rs.createUser(TestUtil.streamObject(userDTO));
            Assert.assertNotNull(response.getEntity());

            System.out.println(response);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Test configure participant email
     */
    @Test
    public void testConfigureParticipantEmail() {

        try {

            List<EmailDTO> emailDTOs = new ArrayList<EmailDTO>(1);
            EmailDTO emailDTO = new EmailDTO();
            emailDTO.setEmailId("vinothini.rajamanickam@object-frontier.com");
            emailDTO.setParticipantId("9908:986559480");
            emailDTOs.add(emailDTO);

            Response response = rs.configureParticipantEmail(TestUtil.streamObject(emailDTOs));
            Assert.assertNotNull(response.getEntity());

            System.out.println(response);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Test get participant email
     */
    @Test
    public void testGetParticipantEmail() {

         try {

            String participantId = "9908:992183888";
            byte[] data = streamObject(participantId);
            Response response = rs.getParticipantEmail(data);
            Assert.assertNotNull(response.getEntity());

            System.out.println(response);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Test download documents
     */
    @Test
    public void testDownloadDocuments() {

         try {

            String fileName = "583229940.6.1525703534976.JavaMail.vinothinir@PC1395.xml";
            
            Response response = rs.downloadDocuments(fileName);
            Assert.assertNotNull(response.getEntity());

            System.out.println(response);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Test delete messages
     */
    @Test
    public void testDeleteMessages() {

        try {

            List<String> messageIds = new ArrayList<String>(1);
            messageIds.add("1369465693.6.1526044944455.JavaMail.vinothinir_PC1395");

            MessageRemoverDTO messageRemoverDTO = new MessageRemoverDTO();
            messageRemoverDTO.setParticipantId("9908:986920080");
            messageRemoverDTO.setMessageIds(messageIds);
            Response response = rs.deleteMessages(TestUtil.streamObject(messageRemoverDTO));
            Assert.assertNotNull(response.getEntity());

            System.out.println(response);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Test validate participant against customization Id
     */
    @Test
    public void testIsValidparticipantAgainstCustomizationID() throws Exception {

        try {

            String participantId = "9908:986559469^" +
                "urn:www.cenbii.eu:transaction:biitrns010:ver2.0:extended:urn:www.peppol.eu:bis:peppol4a:ver2.0:extended:urn:www.difi.no:ehf:faktura:ver2.0";
            Response response = rs.isValidparticipantAgainstCustomizationID(participantId);
            Assert.assertNotNull(response.getEntity());

            System.out.println(response);
        } catch (Exception e) {
            System.err.println(e);
        }
    }
/*    @Test
    public void testRecieve() {
        
        try {
            
            Date now = Calendar.getInstance().getTime();        
            Response response =  rs.receive(TestUtil.streamObject(now));

        } catch (Exception e) {
            System.err.println(e);
        }
    }*/
}
