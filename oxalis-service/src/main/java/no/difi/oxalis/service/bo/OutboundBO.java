/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.difi.oxalis.service.bo;

import eu.peppol.outbound.api.EmailDTO;
import eu.peppol.outbound.api.UserDTO;
import java.io.InputStream;
import java.sql.Array;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.datatype.DatatypeFactory;
import no.difi.oxalis.service.util.ObjectStorage;
import no.difi.oxalis.service.model.MessageInfo;
import eu.peppol.outbound.api.ReceiptDTO;
import no.difi.oxalis.service.util.IdentifierName;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author aktharhussainis
 */
public class OutboundBO extends AbstractBO {
    
    public OutboundBO(String dsName) {
        super(dsName);
    }
     
    public Date getAccessPointLastSyncDateTime(String accesspointId) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        Date lastSyncDateTime = null;

        try {

            final String sql =
                    "SELECT last_sync_datetime FROM accesspoint_settings WHERE accesspoint_id = ?";

            ps = con.prepareStatement(sql);
            ps.setString(1, accesspointId);

            rs = ps.executeQuery();

            while (rs.next()) {
                lastSyncDateTime = rs.getTimestamp(1);
            }
            
        } catch (Throwable e) {
            throw new BOException(e);
        } finally {
            release(rs);
            release(ps);
        }
        
        return lastSyncDateTime;
    }
    
    public MessageInfo getMessage(String messageId) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        MessageInfo messageInfo = null;

        try {

            String sql = "SELECT PM.MESSAGE_IDENTIFIER AS MessageIdentifier, PM.MESSAGE_FILE_NAME AS MessageFileName, PM.SENDER_IDENTIFIER AS SenderIdentifier, "
                    + " PM.DOCUMENT_TYPE_IDENTIFIER AS DocumentIdentifier, PM.RECIPIENT_IDENTIFIER AS RecipientIdentifier, PM.RECEIVED_TIME_STAMP AS receivedTimeStamp, "
                    + " PM.FILE_CONTENT AS fileContent, PM.MESSAGE_READ_FLAG AS MessageReadFlag FROM PEPPOL_MESSAGE_META_DATA PM WHERE PM.IS_DELETED = 0 AND PM.MESSAGE_IDENTIFIER = ? ";

            ps = con.prepareStatement(sql);
            ps.setString(1, messageId);

            rs = ps.executeQuery();

            while (rs.next()) {

                messageInfo = new MessageInfo();
                messageInfo.setMessageId(rs.getString(1));
                messageInfo.setFileName(rs.getString(2));
                messageInfo.setSenderId(rs.getString(3));
                messageInfo.setDocumentId(rs.getString(4));
                messageInfo.setRecipientId(rs.getString(5));

                GregorianCalendar c = new GregorianCalendar();
                c.setTime(rs.getDate(6));
                messageInfo.setTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));

                Blob blob = rs.getBlob(7);
                if (blob != null) {
                    int blobLength = (int) blob.length();
                    messageInfo.setFileData(blob.getBytes(1, blobLength));
                    blob.free();
                } else {
                    messageInfo.setFileData(ObjectStorage.getFile(messageInfo.getFileName()));
                }
                messageInfo.setRead(rs.getBoolean(8));
            }

            return messageInfo;

        } catch (Throwable e) {
            throw new BOException(e);
        } finally {
            release(rs);
            release(ps);
            cleanup();
        }
    }
    
    public List<ReceiptDTO> getReceipts(String messageReference, boolean recentOnly) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ReceiptDTO> receiptInfos = new ArrayList<>();

        try {

            String sql = "SELECT EPEPPOL_MESSAGE_RECEIPT_ID,\n" +
                            "       MESSAGE_REFERENCE,\n" +
                            "       SENDER_PARTICIPANT_ID,\n" +
                            "       RECEIVER_PARTICIPANT_ID,\n" +
                            "       RECEIPT,\n" +
                            "       DESCRIPTION,\n" +
                            "       STATE,\n" +
                            "       CREATED_DATE,\n" +
                            "       MODIFIED_DATE\n" +
                            "FROM EPEPPOL_MESSAGE_RECEIPTS\n";
                    
                    if (messageReference != null) {
                       sql += "WHERE MESSAGE_REFERENCE = ? \n";
                    } else {
                       sql += "WHERE READ_FLAG <> 1 \n"; 
                    }
                            sql += "ORDER BY CREATED_DATE\n";
            
            if(recentOnly) {
                sql += "LIMIT 1";
            }

            ps = con.prepareStatement(sql);
            
            if(messageReference != null) {
                ps.setString(1, messageReference);
            }

            rs = ps.executeQuery();

            while (rs.next()) {

                ReceiptDTO receiptInfo = new ReceiptDTO();
                receiptInfo.setId(rs.getLong(1));
                receiptInfo.setMessageReference(rs.getString(2));
                receiptInfo.setSenderId(rs.getString(3));
                receiptInfo.setReceiverId(rs.getString(4));
                
                Blob blob = rs.getBlob(5);
                if (blob != null) {
                    int blobLength = (int) blob.length();
                    receiptInfo.setReceipt(blob.getBytes(1, blobLength));
                    blob.free();
                } else {
                    receiptInfo.setReceipt(ObjectStorage.getFile(String.valueOf(receiptInfo.getId())));
                }
                
                receiptInfo.setDescription(rs.getString(6));
                receiptInfo.setState(rs.getString(7));
                
                receiptInfo.setCreatedDate(rs.getDate(8));
                receiptInfo.setModifiedDate(rs.getDate(9));
                
                receiptInfos.add(receiptInfo);
            }

            return receiptInfos;

        } catch (Throwable e) {
            throw new BOException(e);
        } finally {
            release(rs);
            release(ps);
            cleanup();
        }
    }
    
    public List<String> getMessageIds(String participantId, boolean retreiveAll) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> messageIdList = new ArrayList<String>();

        try {

            if (participantId != null && (participantId.contains("9908:") || participantId.contains("0192:"))) {

                String sql = "SELECT MESSAGE_IDENTIFIER AS MessageIdentifier FROM PEPPOL_MESSAGE_META_DATA "
                                + " WHERE IS_DELETED = 0 AND RECIPIENT_IDENTIFIER IN ( '" +  participantId.replace("9908:", "0192:") + "' , '" + participantId.replace("0192:", "9908:") + "' ) ";

                if (!retreiveAll) {                   
                    
                   sql = sql+ " AND MESSAGE_READ_FLAG = 0 AND RECEIVED_TIME_STAMP >= DATE_SUB(now(), INTERVAL 6 MONTH)";
                }

                ps = con.prepareStatement(sql);

            } else {

                String sql = "SELECT MESSAGE_IDENTIFIER AS MessageIdentifier FROM PEPPOL_MESSAGE_META_DATA "
                                + " WHERE IS_DELETED = 0 AND RECIPIENT_IDENTIFIER = ? ";

                if (!retreiveAll) {
                   sql = sql+ " AND MESSAGE_READ_FLAG = 0 AND RECEIVED_TIME_STAMP >= DATE_SUB(now(), INTERVAL 6 MONTH)";
                }

                ps = con.prepareStatement(sql);
                ps.setString(1, participantId);                
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                messageIdList.add(rs.getString(IdentifierName.MESSAGE_ID.stringValue()));
            }

            return messageIdList;
        } catch(Exception e) {
            throw new BOException(e);
        } finally {
            release(rs);
            release(ps);
            cleanup();
        }
    }

    public List<String> getMessageIds(java.sql.Timestamp eventDate) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> messageIdList = new ArrayList<String>();

        try {

            String sql = "SELECT MESSAGE_IDENTIFIER AS MessageIdentifier FROM PEPPOL_MESSAGE_META_DATA "
                    + " WHERE IS_DELETED = 0 AND RECEIVED_TIME_STAMP > ? ";

            ps = con.prepareStatement(sql);
            ps.setTimestamp(1, eventDate);

            rs = ps.executeQuery();

            while (rs.next()) {
                messageIdList.add(rs.getString(IdentifierName.MESSAGE_ID.stringValue()));
            }

            return messageIdList;
        } catch(Exception e) {
            throw new BOException(e);
        } finally {
            release(rs);
            release(ps);
            cleanup();
        }
    }
    
    public List<String> getAllUnReadMessageIdForWeb() {

        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> messageIdList = new ArrayList<String>();

        try {

            String sql = "SELECT MESSAGE_IDENTIFIER AS MessageIdentifier FROM PEPPOL_MESSAGE_META_DATA "
                    + " WHERE IS_DELETED = 0 AND READ_FROM_WEB = 0";

            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                messageIdList.add(rs.getString(IdentifierName.MESSAGE_ID.stringValue()));
            }

            return messageIdList;
        } catch(Exception e) {
            throw new BOException(e);
        } finally {
            release(rs);
            release(ps);
            cleanup();
        }
    }
    
    public List<String> getAllUnReadMessageIdOfOrder(String participentId) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> messageIdList = new ArrayList<String>();

        try {

            String sql = "SELECT MESSAGE_IDENTIFIER AS MessageIdentifier FROM PEPPOL_MESSAGE_META_DATA "
                    + " WHERE IS_DELETED = 0 AND MESSAGE_READ_FLAG = 0 " 
                    + " AND ( RECIPIENT_IDENTIFIER = ? OR ? IS NULL )"
                    + " AND ( UPPER(DOCUMENT_TYPE_IDENTIFIER) LIKE '%ORDER%' AND UPPER(DOCUMENT_TYPE_IDENTIFIER) NOT LIKE '%ORDERRESPONSE%' ) ";

            ps = con.prepareStatement(sql);
            
            ps.setString(1, participentId);
            ps.setString(2, participentId);

            rs = ps.executeQuery();

            while (rs.next()) {
                messageIdList.add(rs.getString(IdentifierName.MESSAGE_ID.stringValue()));
            }

            return messageIdList;
        } catch(Exception e) {
            throw new BOException(e);
        } finally {
            release(rs);
            release(ps);
            cleanup();
        }
    }
    
    public List<String> getAllUnReadMessageIdOfOrderResponse(String participentId) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> messageIdList = new ArrayList<String>();

        try {

            String sql = "SELECT MESSAGE_IDENTIFIER AS MessageIdentifier FROM PEPPOL_MESSAGE_META_DATA "
                    + " WHERE IS_DELETED = 0 AND MESSAGE_READ_FLAG = 0 " 
                    + " AND ( RECIPIENT_IDENTIFIER = ? OR ? IS NULL )"
                    + " AND UPPER(DOCUMENT_TYPE_IDENTIFIER) LIKE '%ORDERRESPONSE%' ";

            ps = con.prepareStatement(sql);

            ps.setString(1, participentId);
            ps.setString(2, participentId);
            
            rs = ps.executeQuery();

            while (rs.next()) {
                messageIdList.add(rs.getString(IdentifierName.MESSAGE_ID.stringValue()));
            }

            return messageIdList;
        } catch(Exception e) {
            throw new BOException(e);
        } finally {
            release(rs);
            release(ps);
            cleanup();
        }
    }
    
    public boolean markAsRead(List<String> messageIds) {

        PreparedStatement ps = null;

        try {

            String sql = "UPDATE PEPPOL_MESSAGE_META_DATA SET MESSAGE_READ_FLAG = 1 WHERE MESSAGE_IDENTIFIER IN (?";
            for (int i = 1; i < messageIds.size(); i++) {
                sql = sql + ", ?";
            }
            sql = sql + ")";
            ps = con.prepareStatement(sql);

            for (int i = 0; i < messageIds.size(); i++) {
                ps.setString((i+1), messageIds.get(i));
            }

            ps.executeUpdate();
            return true;

        } catch (Throwable e) {
            throw new BOException(e);
        } finally {
            release(ps);
            cleanup();
        }
    }
    
    public boolean markAsReadFromWeb(List<String> messageIds) {

        PreparedStatement ps = null;

        try {

            String sql = "UPDATE PEPPOL_MESSAGE_META_DATA SET READ_FROM_WEB = 1 WHERE MESSAGE_IDENTIFIER IN (?";
            for (int i = 1; i < messageIds.size(); i++) {
                sql = sql + ", ?";
            }
            sql = sql + ")";
            ps = con.prepareStatement(sql);

            for (int i = 0; i < messageIds.size(); i++) {
                ps.setString((i+1), messageIds.get(i));
            }

            ps.executeUpdate();
            return true;

        } catch (Throwable e) {
            throw new BOException(e);
        } finally {
            release(ps);
            cleanup();
        }
    }
    
    public boolean markReceiptAsRead(List<String> messageIds) {

        PreparedStatement ps = null;

        try {

            String sql = "UPDATE EPEPPOL_MESSAGE_RECEIPTS SET READ_FLAG = 1 WHERE EPEPPOL_MESSAGE_RECEIPT_ID IN (?";
            for (int i = 1; i < messageIds.size(); i++) {
                sql = sql + ", ?";
            }
            sql = sql + ")";
            ps = con.prepareStatement(sql);

            for (int i = 0; i < messageIds.size(); i++) {
                ps.setString((i+1), messageIds.get(i));
            }

            ps.executeUpdate();
            return true;

        } catch (Throwable e) {
            throw new BOException(e);
        } finally {
            release(ps);
            cleanup();
        }
    }
    
    /**
     * Creates the user.
     * 
     * @param userDTO
     *            the user dto
     */
    public void createUser(UserDTO userDTO) {

        PreparedStatement insUserPs = null;
        PreparedStatement insRolePs = null;

        try {
            String insertUserSql =
                    "INSERT INTO users(user_name, password) VALUES (?,?)";

            String insertRoleSql =
                    "INSERT INTO user_role(user_name, role_name) VALUES (?,?)";

            insUserPs = con.prepareStatement(insertUserSql);
            insUserPs.setString(1, userDTO.getUserName());
            insUserPs.setString(2, userDTO.getPassword());
            insUserPs.executeUpdate();

            insRolePs = con.prepareStatement(insertRoleSql);
            insRolePs.setString(1, userDTO.getUserName());
            insRolePs.setString(2, userDTO.getRoleName());
            insRolePs.executeUpdate();

        } catch (Throwable e) {
            throw new BOException(e);
        } finally {
            release(insUserPs);
            release(insRolePs);
            cleanup();
        }
    }

    /**
     * gets existing participants.
     * 
     * @param participants
     *            the participants
     * return participantIds
     */
    private List<String> getExistingParticipants(Set<String> participants) {

        List<String> participantIds = new ArrayList<String>();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT participant_id FROM participant WHERE participant_id IN (?)";

            ps = con.prepareStatement(constructSQLPlaceHolders(sql, participants.size()));

            int i = 1;
            for (String participantId : participants) {
                ps.setString(i++, participantId);
            }
            rs = ps.executeQuery();

            while (rs.next()) {
                participantIds.add(rs.getString(1));
            }

            return participantIds;

        } catch (Throwable e) {
            throw new BOException(e);
        } finally {
            release(rs);
            release(ps);
        }
    }

    /**
     * update participant email.
     * 
     * @param emailDTOs
     *            the email DTOs
     */
    public void updateParticipantEmail(List<EmailDTO> emailDTOs) {

        PreparedStatement insPs = null;
        PreparedStatement updPs = null;

        try {
            final String insertSql =
                    "INSERT INTO participant(participant_id, email_id) VALUES (?,?)";

            final String updateSql = "UPDATE participant SET email_id = ? WHERE participant_id = ?";

            Map<String, EmailDTO> uniqueMap;
            uniqueMap = new HashMap<String, EmailDTO>();

            for (EmailDTO emailDTO : emailDTOs) {
                if (emailDTO.getParticipantId() == null) {
                    continue;
                }
                if (uniqueMap.get(emailDTO.getParticipantId()) == null) {
                    uniqueMap.put(emailDTO.getParticipantId(), emailDTO);
                }
            }

            List<String> existingParticipants = getExistingParticipants(uniqueMap.keySet());

            insPs = con.prepareStatement(insertSql);
            if (!existingParticipants.isEmpty()) {
                updPs = con.prepareStatement(updateSql);
            }

            EmailDTO emailDTO = null;
            for (Map.Entry<String, EmailDTO> entry : uniqueMap.entrySet()) {

                emailDTO = entry.getValue();
                if (existingParticipants.contains(entry.getKey())) {
                    updPs.setString(1, emailDTO.getEmailId());
                    updPs.setString(2, emailDTO.getParticipantId());
                    updPs.addBatch();
                } else {
                    insPs.setString(1, emailDTO.getParticipantId());
                    insPs.setString(2, emailDTO.getEmailId());
                    insPs.addBatch();
                }
            }

            insPs.executeBatch();
            if (!existingParticipants.isEmpty()) {
                updPs.executeBatch();
            }

        } catch (Throwable e) {
            throw new BOException(e);
        } finally {
            release(insPs);
            release(updPs);
            cleanup();
        }
    }

    /**
     * get participant email.
     * 
     * @param participantId
     *            the participant Id
     */
    public String[] getParticipantEmail(String participantId) {

        String[] emailIds = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT email_id FROM participant WHERE participant_id = ? ";

            ps = con.prepareStatement(sql);
            ps.setString(1, participantId);
            rs = ps.executeQuery();

            if (rs.next()) {
                emailIds = (rs.getString(1) != null ? rs.getString(1).split(",") : null);
            }

            return emailIds;

        } catch (Throwable e) {
            throw new BOException(e);
        } finally {
            release(rs);
            release(ps);
            cleanup();
        }
    }

    /**
     * get invoice failure message.
     * 
     * @param messageId
     *            the message Id
     * return auditlog
     */
    public /*AuditLog*/ void getInvoiceFailureMessage(String messageId) {

        messageId = "%" + messageId + "%";

        String comments = null;
        byte[] fileContent =  null;
        Date eventDate = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        //AuditLog auditLog = null;
        String senderId = null;
        String receiverId = null;

        try {
            String sql = "select sender_id, receiver_id, comments, file_content, event_date from audit_log where status = 2 and description like ? ORDER BY EVENT_DATE DESC LIMIT 1 ";

            ps = con.prepareStatement(sql);
            ps.setString(1, messageId);
            rs = ps.executeQuery();

            if (rs.next()) {
                senderId = rs.getString(1);
                receiverId = rs.getString(2);
                comments = rs.getString(3);
                InputStream binaryStream;
                binaryStream = rs.getBinaryStream(4);
                if(binaryStream != null) {
                    fileContent = IOUtils.toByteArray(binaryStream);
                }
                eventDate = rs.getTimestamp(5);
            }

//            auditLog = new AuditLog();
//            auditLog.setSenderId(senderId);
//            auditLog.setReceiverId(receiverId);
//            auditLog.setComments(comments);
//            auditLog.setFileContent(fileContent);
//            auditLog.setEventDate(eventDate);
//            return auditLog;

        } catch (Throwable e) {
            throw new BOException(e);
        } finally {
            release(rs);
            release(ps);
            cleanup();
        }
    }

    /**
     * delete messages.
     * 
     * @param messageIds
     *            the message Ids
     * return true, if successfull
     */
    public boolean deleteMessages(List<String> messageIds) {
        
        PreparedStatement ps = null;

        try {

            String sql = "UPDATE PEPPOL_MESSAGE_META_DATA SET IS_DELETED = 1 WHERE MESSAGE_IDENTIFIER IN (?";
            for (int i = 1; i < messageIds.size(); i++) {
                sql = sql + ", ?";
            }
            sql = sql + ")";

            ps = con.prepareStatement(sql);
            for (int i = 0; i < messageIds.size(); i++) {
                ps.setString((i+1), messageIds.get(i));
            }

            ps.executeUpdate();
            return true;

        } catch (Throwable e) {
            throw new BOException(e);
        } finally {
            release(ps);
            cleanup();
        }
    }
/*    public List<MessageInfo> getAllMessages(java.sql.Timestamp eventDate) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        List<MessageInfo> messageInfoList = new ArrayList<MessageInfo>();
        MessageInfo messageInfo = null;

         try {
            String sql = "SELECT PM.MESSAGE_IDENTIFIER AS MessageIdentifier, PM.MESSAGE_FILE_NAME AS MessageFileName, PM.SENDER_IDENTIFIER AS SenderIdentifier, "
                    + " PM.DOCUMENT_TYPE_IDENTIFIER AS DocumentIdentifier, PM.RECIPIENT_IDENTIFIER AS RecipientIdentifier, PM.RECEIVED_TIME_STAMP AS receivedTimeStamp, "
                    + " PM.FILE_CONTENT AS fileContent, PM.MESSAGE_READ_FLAG AS MessageReadFlag FROM PEPPOL_MESSAGE_META_DATA PM "
                    + " WHERE PM.IS_DELETED = 0 AND PM.RECEIVED_TIME_STAMP > ?";

            ps = con.prepareStatement(sql);
            ps.setTimestamp(1, eventDate);
            rs = ps.executeQuery();

            while (rs.next()) {

                messageInfo = new MessageInfo();
                messageInfo.setMessageId(rs.getString(1));
                messageInfo.setFileName(rs.getString(2));
                messageInfo.setSenderId(rs.getString(3));
                messageInfo.setDocumentId(rs.getString(4));
                messageInfo.setRecipientId(rs.getString(5));

                GregorianCalendar c = new GregorianCalendar();
                c.setTime(rs.getDate(6));
                messageInfo.setTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));

                Blob blob = rs.getBlob(7);
                int blobLength = (int) blob.length();  
                messageInfo.setFileData(blob.getBytes(1, blobLength));
                blob.free();

                messageInfo.setRead(rs.getBoolean(8));
                messageInfoList.add(messageInfo);
            }
             return messageInfoList;

        } catch (Throwable e) {
            throw new BOException(e);
        } finally {
            release(rs);
            release(ps);
            cleanup();
        }
    }
    
    public List<MessageInfo> getAllMessages(String participantId, boolean retreiveAll) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        List<MessageInfo> messageInfoList = new ArrayList<MessageInfo>();
        MessageInfo messageInfo = null;

        try {

            String sql = " SELECT PM.MESSAGE_IDENTIFIER AS MessageIdentifier, PM.MESSAGE_FILE_NAME AS MessageFileName, PM.SENDER_IDENTIFIER AS SenderIdentifier, "
                    + " PM.DOCUMENT_TYPE_IDENTIFIER AS DocumentIdentifier, PM.RECIPIENT_IDENTIFIER AS RecipientIdentifier, PM.RECEIVED_TIME_STAMP AS receivedTimeStamp, "
                    + " PM.FILE_CONTENT AS fileContent, PM.MESSAGE_READ_FLAG AS MessageReadFlag FROM PEPPOL_MESSAGE_META_DATA PM WHERE PM.IS_DELETED = 0 AND PM.RECIPIENT_IDENTIFIER = ? ";

            if (!retreiveAll) {
               sql = sql+ " AND PM.MESSAGE_READ_FLAG = 0";
            }

            sql = sql + " LIMIT 20";

            ps = con.prepareStatement(sql);
            ps.setString(1, participantId);

            rs = ps.executeQuery();

            while (rs.next()) {

                messageInfo = new MessageInfo();
                messageInfo.setMessageId(rs.getString(1));
                messageInfo.setFileName(rs.getString(2));
                messageInfo.setSenderId(rs.getString(3));
                messageInfo.setDocumentId(rs.getString(4));
                messageInfo.setRecipientId(rs.getString(5));

                GregorianCalendar c = new GregorianCalendar();
                c.setTime(rs.getDate(6));
                messageInfo.setTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));

                Blob blob = rs.getBlob(7);
                int blobLength = (int) blob.length();
                messageInfo.setFileData(blob.getBytes(1, blobLength));
                blob.free();

                messageInfo.setRead(rs.getBoolean(8));
                messageInfoList.add(messageInfo);
            }

            return messageInfoList;

        } catch (Throwable e) {
            throw new BOException(e);
        } finally {
            release(rs);
            release(ps);
            cleanup();
        }
    }*/
}
