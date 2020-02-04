/*
 * @(#)ValidationMessageDTO.java
 *
 * Copyright (c) 2013, Zirius AS.
 * All rights reserved. 
 * 
 * Use is subject to license terms. This software is protected by 
 * copyright law and international treaties. Unauthorized reproduction or 
 * distribution of this program, or any portion of it, may result in severe 
 * civil and criminal penalties, and will be prosecuted to the maximum extent.
 */
package ehandel.no.dto;

/**
 * The Class ValidationMessageDTO.
 * 
 * @author kalidasss
 * @since ehf; Apr 25, 2013
 */

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "message")
public class ValidationMessageDTO {

    private String messageType;
    private String title;
    private String description;
    private String schematronRuleId;
    private String hints;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSchematronRuleId() {
        return schematronRuleId;
    }

    public void setSchematronRuleId(String schematronRuleId) {
        this.schematronRuleId = schematronRuleId;
    }

    public String getHints() {
        return hints;
    }

    public void setHints(String hints) {
        this.hints = hints;
    }
}