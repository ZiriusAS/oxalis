/*
 * @(#)SmsDTO.java
 *
 * Copyright (c) 2013, Zirius AS.
 * All rights reserved. 
 * 
 * Use is subject to license terms. This software is protected by 
 * copyright law and international treaties. Unauthorized reproduction or 
 * distribution of this program, or any portion of it, may result in severe 
 * civil and criminal penalties, and will be prosecuted to the maximum extent.
 */
package itella.api;

import java.io.Serializable;

/**
 * The Class SmsDTO.
 * 
 * @author vasanthis
 */
public class SmsDTO {

    private String sender;

    private String cellular;

    private String msg;

    private String userId;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getCellular() {
        return cellular;
    }

    public void setCellular(String cellular) {
        this.cellular = cellular;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
