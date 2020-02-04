/*
 * @(#)ConversionUtils.java
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

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * The Class ConversionUtils.
 * 
 * @author amuthar
 * @since ehf; Feb 15, 2012
 */
public final class ConversionUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * Needed to create XMLGregorianCalendar instances
     */
    private static DatatypeFactory df = null;

    static {
        try {
            df = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException dce) {
            throw new IllegalStateException("Exception while obtaining DatatypeFactory instance",
                    dce);
        }
    }

    /**
     * Converts a java.util.Date into an instance of XMLGregorianCalendar
     * 
     * @param date
     *            Instance of java.util.Date or a null reference
     * @return XMLGregorianCalendar instance whose value is based upon the value in the date
     *         parameter. If the date parameter is null then this method will simply return null.
     */
    public static XMLGregorianCalendar asXMLGregorianCalendar(java.util.Date date) {

        if (date == null) {
            return null;
        }

        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);

        XMLGregorianCalendar xmlgc = df.newXMLGregorianCalendar(gc);
        xmlgc.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
        xmlgc.setTime(DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED,
                DatatypeConstants.FIELD_UNDEFINED);

        return xmlgc;
    }

    /**
     * Converts a java.util.Date into an instance of XMLGregorianCalendar
     * 
     * @param date
     *            Instance of java.util.Date or a null reference
     * @return XMLGregorianCalendar instance whose value is based upon the value in the date
     *         parameter. If the date parameter is null then this method will simply return null.
     */
    public static XMLGregorianCalendar asXMLGregorianCalendarTime(java.util.Date date) {

        if (date == null) {
            return null;
        }

        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);

        XMLGregorianCalendar xmlgc =
                df.newXMLGregorianCalendarTime(gc.get(Calendar.HOUR_OF_DAY),
                        gc.get(Calendar.MINUTE), gc.get(Calendar.SECOND), 0);

        return xmlgc;
    }

    /**
     * As xml gregorian calendar time without zone.
     * 
     * @param date
     *            the date
     * @return the xML gregorian calendar
     */
    public static XMLGregorianCalendar asXMLGregorianCalendarTimeWithoutZone(java.util.Date date) {

        if (date == null) {
            return null;
        }

        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);

        XMLGregorianCalendar xmlgc =
                df.newXMLGregorianCalendarTime(gc.get(Calendar.HOUR_OF_DAY),
                gc.get(Calendar.MINUTE), gc.get(Calendar.SECOND), 0);
        xmlgc.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
        return xmlgc;
    }

    /**
     * Converts an XMLGregorianCalendar to an instance of java.util.Date
     * 
     * @param xgc
     *            Instance of XMLGregorianCalendar or a null reference
     * @return java.util.Date instance whose value is based upon the value in the xgc parameter. If
     *         the xgc parameter is null then this method will simply return null.
     */
    public static Date asDate(XMLGregorianCalendar xgc) {

        if (xgc == null) {
            return null;
        }
        return xgc.toGregorianCalendar().getTime();
    }

    /**
     * Converts double value to BigDecimal
     * 
     * @param dbl
     * @return BigDecimal instance
     */
    public static BigDecimal asBigDecimal(Double dbl) {
        return new BigDecimal("" + dbl.doubleValue());
    }

    /**
     * Get tax category id.
     * 
     * @param taxPercent
     * @return tax category id
     */
    public static String getTaxCategoryCode(Double taxPercent) {

	if (taxPercent == null || taxPercent == 0) {
            return "E";
	} else if (taxPercent > 0 && taxPercent <= 10) {
            return "AA";
	} else if (taxPercent > 10 && taxPercent < 15) {
            return "R";
	} else if (taxPercent >= 15 && taxPercent < 25) {
            return "H";
	} else if (taxPercent >= 25) {
            return "S";
	}
        return "";
    }
    
    public static String getEHFV3TaxCategoryCode(Double taxPercent, String taxCode) {

        if (taxCode != null) {
            
            if (taxCode.equals("20")) { // Tax Free
                return "E";
            }

            if (taxCode.equals("25")) { // Tax Free export
                return "G";
            }

            if (taxCode.equals("29")) { // Services outside scope of tax
                return "O";
            }
        }        

	if (taxPercent == 0) {
            return "E";
	} else if (taxPercent > 0 && taxPercent <= 25) {
            return "S";
	} 
        return "S";
    }

    /**
     * Gets the file stream as byte array.
     * 
     * @param is
     *            the file input stream
     * @return the file as byte array
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static byte[] getFileAsByteArray(InputStream is) throws IOException {

        byte[] blob = null;
        if (is != null) {
            int length = is.available();
            blob = new byte[length];
            is.read(blob);
            is.close();
        }
        return blob;
    }

    /**
     * Gets the rounding amount.
     * 
     * @param amount
     *            the amount
     * @return the rounding amount
     */
    public static BigDecimal getRoundingAmount(Double amount) {

        BigDecimal amt = BigDecimal.valueOf(amount);
        BigDecimal ceiledAmt = BigDecimal.valueOf(Math.ceil(amount));
        return ceiledAmt.subtract(amt);
    }

    public static BigDecimal getRoundingAmount(BigDecimal amount) {

        BigDecimal roundHalfUpAmt = amount.setScale(0, BigDecimal.ROUND_HALF_UP);
        return roundHalfUpAmt.subtract(amount);
    }

    public static BigDecimal add(Double v1, Double v2) {

        BigDecimal bd1 = BigDecimal.valueOf(v1);
        BigDecimal bd2 = BigDecimal.valueOf(v2);
        return bd2.add(bd1);
    }

    /**
     * Round value to the specified number of decimals.
     * 
     * @param value
     *            the value
     * @param places
     *            the decimal places
     * @return the rounded value
     */
    public static double round(double value, int places) {

        double p = Math.pow(10, places);
        value = value * p;
        double tmp = Math.round(value);
        return tmp / p;
    }

    /**
     * Format text containing variables surrounded by braces using the values supplied.
     * 
     * @param text
     *            the text to be formatted
     * @param values
     *            the values to be replaced
     * @return the string
     */
    public static String formatText(String text, Map<String, String> values) {

        if (values == null) {
            return text;
        }
        for (Map.Entry<String, String> entry : values.entrySet()) {
            text = text.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return text;
    }

    public static String stripNonAlphaNumeric(String value) {
        return value.replaceAll("[^0-9]", "");
    }

    public static String removeEmptySpaces(String value) {
        return value.replaceAll("\\W", "");
    }
}
