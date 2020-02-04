package eu.peppol.outbound.api;

import java.util.List;
import java.io.Serializable;

/**
 * 
 * @author vasanthis
 */
public class MessageRemoverDTO implements Serializable {

    private static final long serialVersionUID = 112233445566778804L;

    private String participantId;
    private List<String> messageIds;

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public List<String> getMessageIds() {
        return messageIds;
    }

    public void setMessageIds(List<String> messageIds) {
        this.messageIds = messageIds;
    }
}
