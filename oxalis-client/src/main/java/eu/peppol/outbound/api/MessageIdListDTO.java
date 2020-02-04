/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.peppol.outbound.api;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author dineshkumarrc
 */
public class MessageIdListDTO implements Serializable {

    private static final long serialVersionUID = 112223445566778812L;

    private List<String> messageIdList;
    private Date synchronizedDate;

    public List<String> getMessageIdList() {
        return messageIdList;
    }

    public void setMessageIdList(List<String> messageIdList) {
        this.messageIdList = messageIdList;
    }

    public Date getSynchronizedDate() {
        return synchronizedDate;
    }

    public void setSynchronizedDate(Date synchronizedDate) {
        this.synchronizedDate = synchronizedDate;
    }
}
