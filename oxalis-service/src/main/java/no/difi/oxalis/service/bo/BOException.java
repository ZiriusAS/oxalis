/*
 * @(#)BOException.java
 *
 * Copyright (c) 2012, Zirius AS.
 * All rights reserved. 
 * 
 * Use is subject to license terms. This software is protected by 
 * copyright law and international treaties. Unauthorized reproduction or 
 * distribution of this program, or any portion of it, may result in severe 
 * civil and criminal penalties, and will be prosecuted to the maximum extent.
 */
package no.difi.oxalis.service.bo;

/**
 * The Class BOException.
 * 
 * @author senthilkumarn
 */
public class BOException extends java.lang.RuntimeException {

    public BOException() {
        super();
    }

    public BOException(String message) {
        super(message);
    }

    public BOException(Throwable cause) {
        super(cause);
    }

    public BOException(String message, Throwable cause) {
        super(message, cause);
    }
}
