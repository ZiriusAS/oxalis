/*
 * @(#)StringUtils.java
 *
 * Copyright (c) 2012, Zirius AS.
 * All rights reserved. 
 * 
 * Use is subject to license terms. This software is protected by 
 * copyright law and international treaties. Unauthorized reproduction or 
 * distribution of this program, or any portion of it, may result in severe 
 * civil and criminal penalties, and will be prosecuted to the maximum extent.
 */
package ehandel.no.util;

/**
 * The Class StringUtils.
 * 
 * @author amuthar
 * @since ehf; Mar 19, 2012
 */
public final class StringUtils {

    /**
     * Checks if is empty.
     * 
     * @param text
     *            the text
     * @return true, if is empty
     */
    public static boolean isEmpty(String text) {

        if (text == null || text.trim().length() == 0) {
            return true;
        }
        return false;
    }
}
