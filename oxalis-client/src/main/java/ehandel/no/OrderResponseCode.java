/*
 * @(#)OrderResponseCode.java
 *
 * Copyright (c) 2012, Zirius AS.
 * All rights reserved. 
 * 
 * Use is subject to license terms. This software is protected by 
 * copyright law and international treaties. Unauthorized reproduction or 
 * distribution of this program, or any portion of it, may result in severe 
 * civil and criminal penalties, and will be prosecuted to the maximum extent.
 */
package ehandel.no;

/**
 * The Class OrderResponseCode.
 * 
 * @author vasanthis
 * @since Oct 21, 2013
 */
public enum OrderResponseCode {

    ORDER_REJECTED("RE"), 
    ORDER_ACCEPTED("AP"), 
    CONDITIONALLY_ACCEPTED("CA"),
    MESSAGE_ACKNOWLEDGEMENT("AB");

    private String value;

    private OrderResponseCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
