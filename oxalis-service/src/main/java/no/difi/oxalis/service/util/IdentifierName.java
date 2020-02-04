/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.difi.oxalis.service.util;

/**
 *
 * @author aktharhussainis
 */
public enum IdentifierName {

     MESSAGE_ID         ("MessageIdentifier"),
     CHANNEL_ID         ("ChannelIdentifier"),
     RECIPIENT_ID       ("RecipientIdentifier"),
     SENDER_ID          ("SenderIdentifier"),
     DOCUMENT_ID        ("DocumentIdentifier"),
     PROCESS_ID         ("ProcessIdentifier"),
     SCHEME             ("scheme"),
     MESSAGE_FILE_NAME  ("MessageFileName"),
     TIMESTAMP          ("receivedTimeStamp"),
     MESSAGE_READ_FLAG  ("MessageReadFlag"),
     OLD_TIMESTAMP      ("TimeStamp")
    ;

    String value;

    private IdentifierName(String value){
        this.value = value;
    }

    public String stringValue() {
        return value;
    }

    public static IdentifierName valueOfIdentifier(String stringValue) {
        for (IdentifierName id : IdentifierName.values()) {
          if (id.value.equals(stringValue))
              return id;
        }

        throw new IllegalArgumentException("Unknown identifer: " + stringValue);
    }
}
