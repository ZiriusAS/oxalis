/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.difi.oxalis.service.bo;

import java.io.ByteArrayInputStream;
import java.sql.PreparedStatement;
import no.difi.oxalis.service.model.AuditEvent;
import no.difi.oxalis.service.model.AuditLog;
import no.difi.oxalis.sniffer.identifier.ParticipantId;
import no.difi.oxalis.sniffer.identifier.SchemeId;
import no.difi.vefa.peppol.icd.api.Icd;

/**
 *
 * @author Dinesh
 */
public class AuditLogBO extends AbstractBO {
    
    public AuditLogBO(String dsName) {
        super(dsName);
    }

    public void createAuditLogEntry(AuditLog auditLog) {

        PreparedStatement ps = null;

        try {
            final String sql =
                    "INSERT INTO audit_log(event_id, event_type, sender_id, receiver_id, user_id, license_id, description, event_date, status, comments, file_content) "
                            + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

            ps = con.prepareStatement(sql);

            ps.setString(1, auditLog.getEventId());
            ps.setString(2, auditLog.getEventType());
            ps.setString(3, auditLog.getSenderId());
            ps.setString(4, auditLog.getReceiverId());
            ps.setString(5, auditLog.getUserId());
            ps.setString(6, auditLog.getLicenseId());
            ps.setString(7, auditLog.getDescription());
            ps.setTimestamp(8, new java.sql.Timestamp(auditLog.getEventDate().getTime()));
            ps.setInt(9, auditLog.getStatus());
            ps.setString(10, auditLog.getComments());
            if (auditLog.getFileContent() != null) {
                if (auditLog.getEventType() != AuditEvent.RECEIVE_INVOICE.toString()) {
                    ps.setBinaryStream(11, new ByteArrayInputStream(auditLog.getFileContent()));
                } else {
                    ps.setBinaryStream(11, null);
                }
            } else {
                ps.setBinaryStream(11, null);
            }

            if (ps.executeUpdate() != 1) {
                throw new BOException("Unable to create audit log");
            }
        } catch (Throwable e) {
            throw new BOException(e);
        } finally {
            release(ps);
        }
    }

    String normalize(String s) {
        return s.replaceAll("[:\\/]", "_");
    }

    private String getSchemeId(ParticipantId participant) {
        
        String id = "UNKNOWN:SCHEME";
        if (participant != null) {
            
            String prefix = participant.toString().split(":")[0]; // prefix is the first part (before colon)
            
            Icd scheme = SchemeId.fromISO6523(prefix);
            if (scheme != null) {
                id = scheme.getIdentifier();
            } else {
                id = "UNKNOWN:" + prefix;
            }
        }
        return id;
    }    
}
