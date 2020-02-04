package eu.peppol.outbound.api;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author vasanthis
 */
public class MessageListDTO implements Serializable {

    private static final long serialVersionUID = 112233445566778803L;

    private List<MessageDTO> messages;
    private Date synchronizedDate;

    public List<MessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDTO> messages) {
        this.messages = messages;
    }

    public Date getSynchronizedDate() {
        return synchronizedDate;
    }

    public void setSynchronizedDate(Date synchronizedDate) {
        this.synchronizedDate = synchronizedDate;
    }
}
