package eu.peppol.outbound.api;

import java.io.Serializable;

/**
 * 
 * @author vasanthis
 */
public class EmailDTO implements Serializable {

    private static final long serialVersionUID = 112233445566778805L;

    private String participantId;

    private String emailId;

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
