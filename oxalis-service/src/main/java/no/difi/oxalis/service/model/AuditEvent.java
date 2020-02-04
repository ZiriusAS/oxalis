  
/*
 * @(#)AuditEvent.java
 *
 * Copyright (c) 2020, Zirius AS.
 * All rights reserved. 
 * 
 * Use is subject to license terms. This software is protected by 
 * copyright law and international treaties. Unauthorized reproduction or 
 * distribution of this program, or any portion of it, may result in severe 
 * civil and criminal penalties, and will be prosecuted to the maximum extent.
 */
package no.difi.oxalis.service.model;

/**
 * The Enum AuditEvent.
 * 
 * @author Dinesh
 */
public enum AuditEvent {

    SEND_INVOICE("Send Invoice"),

    RESEND_INVOICE("Resend Invoice"),

    RECEIVE_INVOICE("Receive Invoice"),

    SEND_ORDER("Send Order"),

    SEND_ORDER_RESPONSE("Send Order Response");

    private String value;

    private AuditEvent(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}