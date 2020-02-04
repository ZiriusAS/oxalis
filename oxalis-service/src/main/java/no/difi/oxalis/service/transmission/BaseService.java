/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.difi.oxalis.service.transmission;

import eu.peppol.outbound.api.MessageDTO;
import no.difi.oxalis.service.model.MessageInfo;

/**
 *
 * @author aktharhussainis
 */
public class BaseService {
    
    protected MessageDTO getMessageDTO(MessageInfo msgInfo) {

        MessageDTO msgDTO = new MessageDTO();
        msgDTO.setMessageId(msgInfo.getMessageId());
        msgDTO.setSenderId(msgInfo.getSenderId());
        msgDTO.setReceiverId(msgInfo.getRecipientId());
        msgDTO.setFileData(msgInfo.getFileData());
        msgDTO.setFileName(msgInfo.getFileName());
        msgDTO.setIsRead(msgInfo.isRead());
        if (msgInfo.getTimestamp() != null) {
            msgDTO.setReceivedDate(msgInfo.getTimestamp().toGregorianCalendar().getTime());
        }

        return msgDTO;
    }
}
