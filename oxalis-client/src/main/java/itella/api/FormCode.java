/*
 * @(#)FormCode.java
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

/**
 * The Enum FormCode.
 * 
 * @author vasanthis
 */
public enum FormCode {

    GIRO_TEMPLATE("1"),

    BLANK_TEMPLATE("2");

    private String formCode;

    /**
     * Instantiates a new form code.
     * 
     * @param formCode
     *            the form code
     */
    private FormCode(String formCode) {
        this.formCode = formCode;
    }

    /**
     * Gets the form code.
     * 
     * @return the form code
     */
    public String getFormCode() {
        return formCode;
    }
}
