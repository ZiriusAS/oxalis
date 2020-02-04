/*
 * @(#)ValidationInfoDTO.java
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
 * The Class ValidationInfoDTO.
 * 
 * @author kalidasss
 * @since ehf; Apr 25, 2013
 */

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "messages")
public class ValidationInfoDTO {

    private List<ValidationMessageDTO> validationMessageList;

    private String status;

    private String version;

    @XmlElement(name = "message")
    public List<ValidationMessageDTO> getValidationMessageList() {
        return validationMessageList;
    }

    public void setValidationMessageList(List<ValidationMessageDTO> validationMessageList) {
        this.validationMessageList = validationMessageList;
    }

    @XmlAttribute(name = "valid")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlAttribute(name = "version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}