/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.difi.oxalis.service.controller;

import eu.peppol.outbound.api.DocumentDTO;
import eu.peppol.outbound.api.EmailDTO;
import eu.peppol.outbound.api.MessageDTO;
import eu.peppol.outbound.api.MessageIdListDTO;
import eu.peppol.outbound.api.MessageListDTO;
import eu.peppol.outbound.api.MessageRemoverDTO;
import eu.peppol.outbound.api.UserDTO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.StreamingOutput;
import no.difi.oxalis.service.transmission.OutboundService;
import no.difi.oxalis.service.util.Log;
import no.difi.vefa.peppol.common.model.Endpoint;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author aktharhussainis
 */
@Path("/")
public class RestController extends BaseController {
    
    OutboundService service;
    
    public RestController() {
        this.service = OutboundService.getInstance();
    }

    @POST
    @Path("/send")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response send(@Context SecurityContext sc, byte[] data) {

        try {
            DocumentDTO documentDTO = (DocumentDTO) getObjectFromStream(data);
            String userId = sc.getUserPrincipal().getName();
            String result = service.sendDocument(documentDTO, userId, false);
            
            byte[] reponse = respond(result);
            return Response.ok(reponse).build();
        } catch (Exception e) {
            Log.error("Unable to send file: " + e.getMessage());
            return writeErrorResponse(e);
        }
        
    }
    
    @GET
    @Path("/getMessageInfo/{messageId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessageInfo(@PathParam("messageId") String messageId) {

        try {
            
            Log.info("Get Message Info for messageId: " + messageId);
            MessageDTO messageDTO = service.getMessageInfo(messageId, null);
            
            byte[] reponse = respond(messageDTO);
            return Response.ok(reponse).build();
        } catch (Exception e) {
            Log.error("Unable to get message info: " + e.getMessage());
            return writeErrorResponse(e);
        }
    }
    
    @GET
    @Path("/fetchMessageId/{participant}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchMessageId(@PathParam("participant") String participant) {
        
       try {
            
           Log.info("Fetch Message Id for participant: " + participant);
            MessageIdListDTO messageIdListDTO = service.getMessageIds(participant, null);
            
            byte[] reponse = respond(messageIdListDTO);
            return Response.ok(reponse).build();
        } catch (Exception e) {
            Log.error("Unable to fetch message id list: " + e.getMessage());
            return writeErrorResponse(e);
        }
    }
    
    @POST
    @Path("/receiveMessageId")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response receiveMessageId(byte[] data) {
        
        try {
            
            Date timestamp = (Date) getObjectFromStream(data);
            Log.info("Receive Message Ids for Date: " + timestamp);
            MessageIdListDTO messageIdListDTO = service.getMessageIds(timestamp, null);
            
            byte[] reponse = respond(messageIdListDTO);
            return Response.ok(reponse).build();
        } catch (Exception e) {
            Log.error("Unable to recieve message id list: " + e.getMessage());
            return writeErrorResponse(e);
        }    
    }
    
    @POST
    @Path("/receiveUnReadMessageIdForWeb")
    @Produces(MediaType.APPLICATION_JSON)
    public Response receiveUnReadMessageIdForWeb() {
        
        try {
            
            Log.info("Receive un read Message Ids for Web");
            MessageIdListDTO messageIdList = service.getAllUnReadMessageIdForWeb(null);
            
            byte[] reponse = respond(messageIdList);
            return Response.ok(reponse).build();
        } catch (Exception e) {
            Log.error("Unable to recieve unread message id list: " + e.getMessage());
            return writeErrorResponse(e);
        }
    }
    
    @POST
    @Path("/markAsRead")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response markAsRead(byte[] data) {
        
        try {
            
            List<String> messageIds = (List<String>) getObjectFromStream(data); 
            Log.info("Mark as read for Ids: " + messageIds);
            boolean result = service.markAsRead(messageIds, null);

            byte[] reponse = respond(result);
            return Response.ok(reponse).build();
        } catch (Exception e) {
            Log.error("Unable to mark as read: " + e.getMessage());
            return writeErrorResponse(e);
        }
    }
    
    @POST
    @Path("/markAsReadFromWeb")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response markAsReadFromWeb(byte[] data) {
        
        try {
            
            List<String> messageIds = (List<String>) getObjectFromStream(data); 
            Log.info("Mark as read from Web for Ids: " + messageIds);
            boolean result = service.markAsReadFromWeb(messageIds, null);

            byte[] reponse = respond(result);
            return Response.ok(reponse).build();
        } catch (Exception e) {
            Log.error("Unable to mark as read from web: " + e.getMessage());
            return writeErrorResponse(e);
        }
    }
    
    @POST
    @Path("/getAccesspointDetails")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccesspointDetails(byte[] data) {
        
        try {
            
            String participantId = (String) getObjectFromStream(data); 
            Log.info("Get Accesspoint Details for participant id: " + participantId);
            Endpoint endpoint = service.getAccesspointDetails(participantId, null);
            String endPointData = "";
            if (endpoint != null) {
                String[] endpointArray = endpoint.toString().split(",");
                endPointData = endpointArray[1] + "," + endpointArray[0] + "," + endpointArray[3];
            } else {
                throw new Exception("For Participant " + participantId + ": Accesspoint Details not found");
            }
            
            byte[] reponse = respond(endPointData);
            return Response.ok(reponse).build();
        } catch (Exception e) {
            Log.error("Unable to get Accesspoint Details: " + e.getMessage());
            return writeErrorResponse(e);
        }
    }
    
    @POST
    @Path("/getEHFV3InvoiceEndPoint")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEHFV3InvoiceEndPoint(byte[] data) {
        
        try {
            
            String participantId = (String) getObjectFromStream(data); 
            Log.info("Get Accesspoint Details for participant id if EHFV3 enabled: " + participantId);
            Endpoint endpoint = service.getEHFV3InvoiceEndPoint(participantId, null);
            String endPointData = "";
            if (endpoint != null) {
                String[] endpointArray = endpoint.toString().split(",");
                endPointData = endpointArray[1] + "," + endpointArray[0] + "," + endpointArray[3];
            } else {
                throw new Exception("For Participant " + participantId + ": Accesspoint Details not found/EHFV3 Invoice not enabled");
            }
            
            byte[] reponse = respond(endPointData);
            return Response.ok(reponse).build();
        } catch (Exception e) {
            Log.error("Unable to get Accesspoint Details: " + e.getMessage());
            return writeErrorResponse(e);
        }
    }
    
    @POST
    @Path("/getEHFV3CreditNoteEndPoint")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEHFV3CreditNoteEndPoint(byte[] data) {
        
        try {
            
            String participantId = (String) getObjectFromStream(data); 
            Log.info("Get Accesspoint Details for participant id if EHFV3 enabled: " + participantId);
            Endpoint endpoint = service.getEHFV3CreditNoteEndPoint(participantId, null);
            String endPointData = "";
            if (endpoint != null) {
                String[] endpointArray = endpoint.toString().split(",");
                endPointData = endpointArray[1] + "," + endpointArray[0] + "," + endpointArray[3];
            } else {
                throw new Exception("For Participant " + participantId + ": Accesspoint Details not found/EHFV3 CreditNote not enabled");
            }
            
            byte[] reponse = respond(endPointData);
            return Response.ok(reponse).build();
        } catch (Exception e) {
            Log.error("Unable to get Accesspoint Details: " + e.getMessage());
            return writeErrorResponse(e);
        }
    }
    
    @GET
    @Path("/getAccesspointDetails/{participantId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccesspointDetails(@PathParam("participantId") String participantId) {
        
        try {
            
            Log.info("Get Accesspoint Details for participant id: " + participantId);
            Endpoint endpoint = service.getAccesspointDetails(participantId, null);
            String endPointData = "";
            if (endpoint != null) {
                String[] endpointArray = endpoint.toString().split(",");
                endPointData = endpointArray[1] + "," + endpointArray[0] + "," + endpointArray[3];
            } else {
               throw new Exception("For Participant " + participantId + ": Accesspoint Details not found");
            }
            
            byte[] reponse = respond(endPointData);
            return Response.ok(reponse).build();
        } catch (Exception e) {
            Log.error("Unable to get Accesspoint Details: " + e.getMessage());
            return writeErrorResponse(e);
        }
    }

    @GET
    @Path("/validate/{participant}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response validate(@PathParam("participant") String participant) {
        
        try {

            Log.info("Check for participant id: " + participant);
            Endpoint endpoint = service.getAccesspointDetails(participant, null);
            
            byte[] reponse = respond(endpoint != null);
            return Response.ok(reponse).build();
        } catch (Exception e) {
            Log.error("Unable to Check for participant id: " + e.getMessage());
            return writeErrorResponse(e);
        }
    }

    @GET
    @Path("/getFileContentByFileName/{fileName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFileContentByFileName(@PathParam("fileName") String fileName) {

        try {

            Log.info("Get File Content By FileName: " + fileName);
            byte[] fileContent = service.getFileContentByFileName(fileName);
            return Response.ok(fileContent).build();
        } catch (Exception e) {
            Log.error("Unable to get File Content By FileName: " + e.getMessage());
            return writeErrorResponse(e);
        }
    }

    @GET
    @Path("/getFollowUpDocuments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFollowUpDocuments() {

        try {

            MessageListDTO messages = service.getFollowUpDocuments();
            return Response.ok(messages).build();
        } catch (Exception ex) {

            Log.error("Unable to get followup documents " + ex.getMessage());
            return writeErrorResponse(ex);
        }
    }

    @POST
    @Path("/sendFollowUpDocuments")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendFollowUpDocuments(MessageDTO message) {

        try {

            boolean result = service.sendFollowUpDocuments(message);
            return Response.ok(result).build();
        } catch (Exception e) {

            Log.error("Unable to send followup documents " + e.getMessage());
            return writeErrorResponse(e);
        }
    }

    @POST
    @Path("/createUser")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(byte[] data) {

        try {
            UserDTO userDTO = (UserDTO) getObjectFromStream(data);
            boolean result = service.createUser(userDTO);

            byte[] reponse = respond(result);
            return Response.ok(reponse).build();
        } catch (Exception e) {

            Log.error("Unable to create user: " + e.getMessage());
            return writeErrorResponse(e);
        }
    }

    @POST
    @Path("/configureParticipantEmail")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response configureParticipantEmail(byte[] data) {

        try {
            List<EmailDTO> emailDTOs = (List<EmailDTO>) getObjectFromStream(data);
            boolean result = service.configureParticipantEmail(emailDTOs);

            byte[] reponse = respond(result);
            return Response.ok(reponse).build();
        } catch (Exception e) {
            Log.error("Unable to configure participant email: " + e.getMessage());
            return writeErrorResponse(e);
        }
    }
    
    @POST
    @Path("/getParticipantEmail")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getParticipantEmail(byte[] data) {

        try {

            String participantId = (String) getObjectFromStream(data); 
            Log.info("Get participant email By participantId: " + participantId);
            String[] participantEmail = service.getParticipantEmail(participantId);
            
            byte[] reponse = respond(participantEmail);
            return Response.ok(reponse).build();
        } catch (Exception e) {
            Log.error("Unable to get participant email By participantId: " + e.getMessage());
            return writeErrorResponse(e);
        }
    }

    @GET
    @Path("/download/{fileName}")
    public Response downloadDocuments(String fileName) {

        try {
            final File file = service.downloadDocuments(fileName, null);
            if (file == null) {
                throw new Exception(fileName + " does not exist");
            }
            StreamingOutput fileStream =  new StreamingOutput() {
                @Override
                public void write(java.io.OutputStream output) throws IOException {

                    output.write(IOUtils.toByteArray(new FileInputStream(file)));
                    output.flush();
                }
            };
            return Response.ok(fileStream, MediaType.APPLICATION_OCTET_STREAM).header("Content-Disposition",
                        "attachment;filename="+fileName).build();

        } catch (Exception e) {
            Log.error("Unable to download: " + e.getMessage());
            return writeErrorResponse(e);
        }
    }

    @POST
    @Path("/remove")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMessages(byte[] data) {

        try {

            MessageRemoverDTO messageRemoverDTO = (MessageRemoverDTO) getObjectFromStream(data);
            boolean result = service.deleteMessages(messageRemoverDTO, null);
            
            byte[] reponse = respond(result);
            return Response.ok(reponse).build();
        } catch (Exception e) {
            Log.error("Unable to delete messages: " + e.getMessage());
            return writeErrorResponse(e);
        }
    }

    @GET
    @Path("/validateAgainstCustomizationID/{participantId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response isValidparticipantAgainstCustomizationID(@PathParam("participantId") String participantId) {

        try {

            boolean result = service.isValidparticipantAgainstCustomizationID(participantId, null);
            
            byte[] reponse = respond(result);
            return Response.ok(reponse).build();
        } catch (Exception e) {
            Log.error("Unable to validate participant: " + e.getMessage());
            return writeErrorResponse(e);
        }
    }
/*  @POST
    @Path("/receive")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public Response receive(byte[] data) {
        
        MessageListDTO messageListDTO = null;
        
        try {
            
            Date timestamp = (Date) getObjectFromStream(data);
            messageListDTO = service.getAllMessages(timestamp, null);
        } catch (Exception e) {
            return Response.serverError().build();
        }
        
        return Response.ok(messageListDTO).build();
    }*/
}
